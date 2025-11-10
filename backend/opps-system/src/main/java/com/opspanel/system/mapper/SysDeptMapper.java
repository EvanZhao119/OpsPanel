package com.opspanel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.system.domain.SysDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Mapper interface for department operations.
 */
@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * Retrieve a department by name.
     *
     * @param deptName department name
     * @return the matching department entity or null if not found
     */
    @Select("SELECT * FROM sys_dept WHERE dept_name = #{deptName} AND deleted = 0")
    SysDept selectByName(@Param("deptName") String deptName);
}
