# APNS  VOIP推送 java版本 

修改推送方式   使用VOIP的用这一句   String topic = "com.xxxx.voip";  
             使用APNS的用这一句   String topic = "com.xxxx";  
 设置token   String deviceToken ="a4acab463ab55b9342d10abcf47787a6c6cb99881c2268dee312f001c86a2739";  
 修改     apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST) 这里的 HOST  
修改配置的证书，使用p12证书  
 &ensp;&ensp;  apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)  
  &ensp;&ensp;                      .setClientCredentials(new File("/Users/as/Desktop/apnstuisong.p12"),"123456")  
 &ensp;&ensp;                      .setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup).build();  
