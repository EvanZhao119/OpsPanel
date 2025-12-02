<template>
    <div class="insight-chat-panel">
      <!-- ======================= Header / Session Toolbar ======================= -->
      <div v-if="showSessionToolbar" class="insight-header">
        <div class="left">
          <span class="title">AI Insight</span>
          <el-select
            v-model="currentSessionId"
            placeholder="Insight Session"
            size="small"
            style="min-width: 200px; margin-left: 8px"
            @change="onSessionChange"
          >
            <el-option
              v-for="session in sessions"
              :key="session.id"
              :label="getSessionLabel(session)"
              :value="session.id"
            />
          </el-select>
  
          <el-button
            size="small"
            type="primary"
            text
            class="ml-4"
            @click="createNewSession"
          >
            New
          </el-button>
        </div>
  
        <div class="right">
          <el-button
            v-if="currentSessionId"
            size="small"
            type="danger"
            text
            @click="handleCloseSession"
          >
            Close
          </el-button>
        </div>
      </div>
  
      <!-- ======================= Chat Body ======================= -->
      <div class="insight-body" ref="messageContainer">
        <div v-if="currentMessages.length === 0" class="empty-tip">
          <p>No messages yet. Ask the AI about your dashboard or system.</p>
          <p class="sub">
            Example: "What can you observe from the task failure rate?"
          </p>
        </div>
  
        <div
          v-for="(msg, index) in currentMessages"
          :key="index"
          class="message-row"
          :class="msg.role.toLowerCase()"
        >
          <div class="avatar">
            <span v-if="msg.role === 'USER'">U</span>
            <span v-else>AI</span>
          </div>
          <div class="bubble">
            <div class="bubble-role">
              {{ msg.role === 'USER' ? 'You' : 'Assistant' }}
            </div>
            <div class="bubble-content">
              {{ msg.content }}
            </div>
          </div>
        </div>
      </div>
  
      <!-- ======================= Input Area ======================= -->
      <div class="insight-footer">
        <el-input
          v-model="inputText"
          type="textarea"
          :rows="3"
          resize="none"
          placeholder="Type your question here. Press Enter to send, Shift+Enter for a new line."
          @keyup.enter.exact.prevent="handleSend"
        />
        <div class="footer-actions">
          <span class="hint">Enter to send, Shift+Enter for new line.</span>
          <el-button
            type="primary"
            size="small"
            :loading="sending"
            @click="handleSend"
          >
            Send
          </el-button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  /* ------------------------------------------------------------
   * Reusable AI Insight Chat Panel
   * ------------------------------------------------------------
   * Back-end controllers used:
   *  - POST /api/insight/chat
   *  - GET  /api/insight/session/list
   *  - POST /api/insight/session/{id}/close
   *  - GET  /api/insight/message/listBySession/{id}
   * ------------------------------------------------------------ */
  
  import { ref, reactive, computed, onMounted, nextTick } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  import { chatInsight } from '@/api/insight/chat'
  import { listInsightSessions, closeInsightSession } from '@/api/insight/session'
  import { listInsightMessagesBySession } from '@/api/insight/message'
  
  /* ---------------------- Props ---------------------- */
  /**
   * sourceType: where the chat is triggered, e.g. "DASHBOARD" / "INSIGHT"
   * sourceRef:  dashboard page id or other reference
   * contextJson: dashboard context (object or JSON string)
   * showSessionToolbar: show or hide session selector in header
   */
  const props = defineProps({
    sourceType: {
      type: String,
      default: 'INSIGHT'
    },
    sourceRef: {
      type: [String, Number, null],
      default: null
    },
    contextJson: {
      type: [Object, String, null],
      default: null
    },
    showSessionToolbar: {
      type: Boolean,
      default: true
    }
  })
  
  /* ---------------------- Local State ---------------------- */
  
  const sessions = ref([])             // session list from backend
  const currentSessionId = ref(null)   // current selected session id or null
  
  // messages are cached by session id, or "temp" for a not-yet-persisted session
  const sessionMessages = reactive({
    temp: []
  })
  
  const inputText = ref('')
  const sending = ref(false)
  const messageContainer = ref(null)
  
  /* ---------------------- Helpers ---------------------- */
  
  function createMessage(role, content) {
    return { role, content }
  }
  
  function getSessionLabel(session) {
    if (!session) return ''
    if (session.title) return session.title
    return `Session #${session.id}`
  }
  
  /**
   * Get message list for current session.
   */
  const currentMessages = computed(() => {
    const key = currentSessionId.value || 'temp'
    if (!sessionMessages[key]) {
      sessionMessages[key] = []
    }
    return sessionMessages[key]
  })
  
  async function scrollToBottom() {
    await nextTick()
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    }
  }
  
  /* ---------------------- Load sessions + history ---------------------- */
  
  async function loadSessions() {
    try {
      const res = await listInsightSessions()
      const list = res.data || res || []
      sessions.value = list
  
      // keep currentSessionId if already set
      if (!currentSessionId.value && sessions.value.length > 0) {
        currentSessionId.value = sessions.value[0].id
      }
  
      if (currentSessionId.value) {
        await loadMessagesForSession(currentSessionId.value)
      }
    } catch (e) {
      console.error('Failed to load insight sessions:', e)
    }
  }
  
  /**
   * Load history messages for a given session from backend.
   */
  async function loadMessagesForSession(sessionId) {
    if (!sessionId) {
      // new (temp) session: keep local buffer only
      if (!sessionMessages.temp) {
        sessionMessages.temp = []
      }
      return
    }
  
    try {
      const res = await listInsightMessagesBySession(sessionId)
      const list = res.data || res || []
  
      sessionMessages[sessionId] = (list || []).map(m => {
        const role =
          (m.role || '').toUpperCase() === 'USER' ? 'USER' : 'ASSISTANT'
        return {
          role,
          content: m.content || ''
        }
      })
  
      await scrollToBottom()
    } catch (e) {
      console.error('Failed to load insight messages:', e)
      sessionMessages[sessionId] = [
        createMessage('ASSISTANT', 'Failed to load history messages.')
      ]
    }
  }
  
  /* ---------------------- Session change / new / close ---------------------- */
  
  function onSessionChange(id) {
    const key = id || 'temp'
    if (!sessionMessages[key]) {
      sessionMessages[key] = []
    }
    currentSessionId.value = id || null
    // load history if this is a persisted session
    if (id) {
      loadMessagesForSession(id)
    }
  }
  
  /**
   * Create a brand new session (local only before first chat).
   */
  function createNewSession() {
    currentSessionId.value = null
    sessionMessages.temp = []
  }
  
  /* ---------------------- Chat ---------------------- */
  
  async function handleSend() {
    const text = (inputText.value || '').trim()
    if (!text || sending.value) return
  
    currentMessages.value.push(createMessage('USER', text))
    inputText.value = ''
    await scrollToBottom()
  
    sending.value = true
    try {
      let contextStr = null
      if (props.contextJson != null) {
        if (typeof props.contextJson === 'string') {
          contextStr = props.contextJson
        } else {
          contextStr = JSON.stringify(props.contextJson)
        }
      }
  
      const payload = {
        sessionId: currentSessionId.value || null,
        sourceType: props.sourceType,
        sourceRef:
          props.sourceRef === null || props.sourceRef === undefined
            ? null
            : String(props.sourceRef),
        question: text,
        contextJson: contextStr
      }
  
      const res = await chatInsight(payload)
      const api = res?.data ?? res
      const data = api?.data ?? api
  
      if (!data) {
        throw new Error('Empty response from insight service')
      }
  
      // if this is a new session, move temp messages to the real session bucket
      if (!currentSessionId.value) {
        const newId = data.sessionId
        if (!newId) {
          throw new Error('Insight response does not contain sessionId')
        }
  
        const tempMessages = sessionMessages.temp || currentMessages.value
        delete sessionMessages.temp
        sessionMessages[newId] = tempMessages
        currentSessionId.value = newId
  
        // reload session list but keep currentSessionId
        await loadSessions()
      }
  
      currentMessages.value.push(
        createMessage('ASSISTANT', data.answer || '')
      )
      await scrollToBottom()
    } catch (e) {
      console.error('Insight chat error:', e)
      currentMessages.value.push(
        createMessage(
          'ASSISTANT',
          'Sorry, something went wrong while contacting the AI service.'
        )
      )
      await scrollToBottom()
    } finally {
      sending.value = false
    }
  }
  
  /* ---------------------- Close session ---------------------- */
  
  async function handleCloseSession() {
    if (!currentSessionId.value) return
  
    try {
      await ElMessageBox.confirm(
        'Are you sure you want to close this session?',
        'Confirm',
        {
          type: 'warning'
        }
      )
    } catch {
      return
    }
  
    try {
      await closeInsightSession(currentSessionId.value)
      const closedId = currentSessionId.value
  
      sessions.value = sessions.value.filter(s => s.id !== closedId)
      // you can choose to keep or delete local messages
      // delete sessionMessages[closedId]
  
      if (sessions.value.length > 0) {
        currentSessionId.value = sessions.value[0].id
        await loadMessagesForSession(currentSessionId.value)
      } else {
        currentSessionId.value = null
        if (!sessionMessages.temp) {
          sessionMessages.temp = []
        }
      }
  
      ElMessage.success('Session closed')
    } catch (e) {
      console.error('Failed to close session:', e)
      ElMessage.error('Failed to close session')
    }
  }
  
  /* ---------------------- Init ---------------------- */
  
  onMounted(() => {
    loadSessions()
  })
  </script>
  
  <style scoped>
  .insight-chat-panel {
    display: flex;
    flex-direction: column;
    height: 100%;
    background-color: #f9fafb;
  }
  
  /* header */
  .insight-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 10px;
    border-bottom: 1px solid #e5e7eb;
    background-color: #ffffff;
  }
  
  .insight-header .left {
    display: flex;
    align-items: center;
  }
  
  .insight-header .title {
    font-weight: 600;
    font-size: 14px;
  }
  
  .ml-4 {
    margin-left: 4px;
  }
  
  /* body */
  .insight-body {
    flex: 1;
    padding: 8px 10px;
    overflow-y: auto;
  }
  
  .empty-tip {
    margin-top: 32px;
    text-align: center;
    color: #9ca3af;
  }
  
  .empty-tip .sub {
    font-size: 12px;
  }
  
  /* messages */
  .message-row {
    display: flex;
    margin-bottom: 8px;
  }
  
  .message-row.user {
    flex-direction: row-reverse;
  }
  
  .message-row .avatar {
    width: 26px;
    height: 26px;
    border-radius: 50%;
    font-size: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #e5e7eb;
    color: #374151;
    margin: 0 6px;
  }
  
  .message-row.assistant .avatar {
    background-color: #3b82f6;
    color: #ffffff;
  }
  
  .message-row .bubble {
    max-width: 80%;
    padding: 6px 9px;
    border-radius: 8px;
    background-color: #ffffff;
    box-shadow: 0 1px 2px rgba(15, 23, 42, 0.05);
  }
  
  .message-row.user .bubble {
    background-color: #e0f2fe;
  }
  
  .bubble-role {
    font-size: 11px;
    color: #6b7280;
    margin-bottom: 2px;
  }
  
  .bubble-content {
    font-size: 13px;
    white-space: pre-wrap;
    word-break: break-word;
  }
  
  /* footer */
  .insight-footer {
    border-top: 1px solid #e5e7eb;
    padding: 8px 10px;
    background-color: #ffffff;
  }
  
  .footer-actions {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 4px;
  }
  
  .footer-actions .hint {
    font-size: 11px;
    color: #9ca3af;
  }
  </style>
  