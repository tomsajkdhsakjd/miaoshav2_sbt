package com.shendehaizi.mapper;


import com.shendehaizi.entity.Seckill;

public interface SeckillDao {

	int delSeckillone(long seckillId);
	/**
	 * 根据ID查询秒杀对象
	 * @param seckillId
	 * @return
	 */
	Seckill querybyid(long seckillId);

	void addseckillnum(Integer seckild_id);
}
