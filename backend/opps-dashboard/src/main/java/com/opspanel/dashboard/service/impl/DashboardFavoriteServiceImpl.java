package com.opspanel.dashboard.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.dashboard.domain.entity.DashboardFavorite;
import com.opspanel.dashboard.mapper.DashboardFavoriteMapper;
import com.opspanel.dashboard.service.DashboardFavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardFavoriteServiceImpl
        extends ServiceImpl<DashboardFavoriteMapper, DashboardFavorite>
        implements DashboardFavoriteService {

    @Override
    public List<DashboardFavorite> listByUserId(Long userId) {
        return lambdaQuery()
                .eq(DashboardFavorite::getUserId, userId)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setHomePage(Long userId, Long pageId) {
        DashboardFavorite currentHome = lambdaQuery()
                .eq(DashboardFavorite::getUserId, userId)
                .eq(DashboardFavorite::getPageId, pageId)
                .eq(DashboardFavorite::getIsHome, 1)
                .one();

        if (currentHome != null) {
            currentHome.setIsHome(0);
            updateById(currentHome);
            return;
        }

        lambdaUpdate()
                .eq(DashboardFavorite::getUserId, userId)
                .set(DashboardFavorite::getIsHome, 0)
                .update();

        DashboardFavorite favorite = lambdaQuery()
                .eq(DashboardFavorite::getUserId, userId)
                .eq(DashboardFavorite::getPageId, pageId)
                .one();

        if (favorite == null) {
            favorite = new DashboardFavorite();
            favorite.setUserId(userId);
            favorite.setPageId(pageId);
        }

        favorite.setIsHome(1);
        saveOrUpdate(favorite);
    }
}
