window.onload=function(){
	seckill.detail.init();	
}
var seckill={
	//验证手机格式
	validataPhone:function(phone){
		if(phone!=null&&phone.length==11&&!isNaN(phone))
		{
			return true;
		}else
		{
			return false;
		}
	},
    	URL:{
    		now:function(){

    			return "/seckill/getnowtime";
    		}

    	},
		 //详情页秒杀逻辑
        detail:{
        	init:function(){
				var userPhone=$.cookie('userPhone');

				//验证绑定的手机
				if(!seckill.validataPhone(userPhone)){

					//绑定手机 控制输出
					var killPhoneModal = $('#killPhoneModal');
					killPhoneModal.modal({
						show: true,//显示弹出层
						backdrop: 'static',//禁止位置关闭
						keyboard: false//关闭键盘事件
					});
				}
				$('#killPhoneBtn').click(function () {
					var inputPhone = $('#killphoneKey').val();
					console.log("inputPhone: " + inputPhone);
					if (seckill.validataPhone(inputPhone)) {
						//电话写入cookie(7天过期)
						alert(333)
						$.cookie('userPhone', inputPhone, {expires: 7, path: '/'});
						//验证通过　　刷新页面
						alert(3333)
						window.location.reload();
					} else {
						//todo 错误文案信息抽取到前端字典里
						$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
					}
				});
        		 var startTime=new Date($('#startTime').html());
        		var endTime=new Date($('#endTime').html());
        		 seckillId = $('#seckillId').html();
        		 $.get(seckill.URL.now(),{},function(result){
        			  if (result && result['msg']) {
                          var nowTime = result['msg'];
                          //时间判断 计时交互
                          seckill.countDown(seckillId, nowTime, startTime, endTime);
                      } else {
                          console.log('result: ' + result);
                          alert('result: ' + result);
                      }

        		 })



        	}

        },
        handlerSeckill:function(seckillId, node){
        	 //获取秒杀地址,控制显示器,执行秒杀
            node.hide().html('<button  id="killBtn"  type="button" >开启秒杀</button>');
        	$.get('/seckill/'+seckillId+'/getkillurl',{},function(result){
        		if(result&&result['msg']){
        			//开启秒杀
                    //获取秒杀地址
                    var md5 = result['msg'];

                    var killUrl='/seckill/' + seckillId + '/' + md5 + '/execution';
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1.先禁用按钮
					var stop=window.setInterval(function () {

							$.get(killUrl,
								{},
								function (result) {
								console.log(result)
									if (result && result['success']) {

										if(result['successmiaosha']){
											node.html('<button class="btn btn-primary btn-lg" id="killBtn1">请在3分钟内执行下单 </button>');

											$('#killBtn1').one('click', function () {
												$.get('/seckill/'+seckillId+'/submitmessage',{},function(result){
													node.show();
													node.html('<span class="label label-success">' + result['stateInfo'] + '</span>');

												})

											})

										}else{
											var data=result['data']
											node.html('<span class="label label-success">' + data['stateInfo'] + '</span>');
										}




									}else {
										//clearInterval(stop);
									}
								});
						}, 1000);

                       });
                       node.show();
        		}


        	})


        },
    	countDown:function(seckillId, nowTime1, startTime, endTime){
    	    var nowTime=new Date(nowTime1);
			var seckillBox = $('#seckill-box');
    		var node=$('kill');
    		if(nowTime>endTime){
                //秒杀结束
                seckillBox.html('秒杀结束!');

    		}else if(nowTime<startTime){

                seckillBox.countdown(startTime, function (event) {
                    //时间格式
                	  var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                    seckillBox.html(format);
                }).on('finish.countdown', function () {
                    //时间完成后回调事件
                    //获取秒杀地址,控制现实逻辑,执行秒杀

                    seckill.handlerSeckill(seckillId,seckillBox);
                });
    		}else{

    			seckill.handlerSeckill(seckillId,seckillBox);
    		}
    	}
}

