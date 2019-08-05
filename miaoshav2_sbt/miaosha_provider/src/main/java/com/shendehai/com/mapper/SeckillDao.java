package com.shendehai.com.mapper;

import com.shendehai.com.common.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

	 /**
     * 根据ID查询秒杀对象
     * @param seckillId
     * @return
     */
	Seckill querybyid(long seckillId);

    /**
     * 根据偏移量来查询秒杀商品列表
     * @param offet
     * @param limit
     * @return
     */
	List<Seckill> queryAll();
	Integer delSeckillone(long seckillId);
}
