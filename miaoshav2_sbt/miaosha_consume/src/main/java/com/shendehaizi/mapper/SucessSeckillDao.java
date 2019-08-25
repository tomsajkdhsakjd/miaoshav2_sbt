package com.shendehaizi.mapper;

import org.apache.ibatis.annotations.Param;


public interface SucessSeckillDao {
	/**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数，返回0代表插入失败
     */
	public int insertSucessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);


    void delnotpayorder(Integer seckild_id);
}
