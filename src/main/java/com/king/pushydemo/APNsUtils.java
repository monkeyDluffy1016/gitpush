package com.king.pushydemo;

import com.turo.pushy.apns.*;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;
import java.util.UUID;



@Slf4j
public class APNsUtils {
    private static ApnsClient apnsClient = null;
    public static void main(String[] args) throws Exception {
        //IOS等终端设备注册后返回的DeviceToken
        String deviceToken ="426333051ace4aa9e3dbd27d24f99f15b7f73b5a3a6e092e89a57493ebcb66b8";
//        String deviceToken = "74abd7d51c58a8db995fa53c3508a72481a9d4ad56e7ed1f7d12362f798a6906";
        /**
         * Use the voip push type for notifications that provide information about an incoming Voice-over-IP (VoIP)
         * call. For more information, see Responding to VoIP Notifications from PushKit.
         * If you set this push type, the apns-topic header field must use your app’s bundle ID with .voip
         * appended to the end. If you’re using certificate-based authentication,
         * you must also register the certificate for VoIP services.
         * The topic is then part of the 1.2.840.113635.100.6.3.4 or 1.2.840.113635.100.6.3.6 extension.
         */
        //这是你的主题，大多数情况是bundleId，voip需要在bundleId加上.voip。对应文档中的apns-topic
        //此处可以参考https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/sending_notification_requests_to_apns?language=objc

//        String topic = "com.chinaums.umschat2.voip";
        String topic = "com.chinaums.umschat2";
        String payload = "{ \"aps\" : {\"alert\" : \"我的打井\", \"sound\" : \"default\", \"badge\" :1},\"liguoxin\":\"liguoxin\" }";
        //有效时间
        Date invalidationTime= new Date(System.currentTimeMillis() + 60 * 60 * 1000L );
        //发送策略 apns-priority 10为立即 5为省电
        DeliveryPriority priority= DeliveryPriority.IMMEDIATE;
        //推送方式，主要有alert，background，voip，complication，fileprovider，mdm
//        PushType pushType = VOIP;
        //推送的合并ID，相同的 apns-collapse-id会在App中合并
        String collapseId= UUID.randomUUID().toString();
        //apnsId 唯一标示，如果不传，APNs会给我们生成一个
        UUID apnsId = UUID.randomUUID();
        //构造一个APNs的推送消息实体
        SimpleApnsPushNotification msg = new SimpleApnsPushNotification(deviceToken,topic,payload);
        //开始推送
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future = getAPNSConnect().sendNotification(msg);
        PushNotificationResponse<SimpleApnsPushNotification> response = future.get();
        System.out.println(response.getRejectionReason());
        //如果返回的消息中success为true那么成功，否则失败！
        //如果失败不必惊慌，rejectionReason字段中会有失败的原因。对应官网找到原因即可
        //https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/handling_notification_responses_from_apns?language=objc

        System.out.println("------------->"+response);
    }

    public static ApnsClient getAPNSConnect() {

        if (apnsClient == null) {
            try {
                /**
                 * Development server: api.sandbox.push.apple.com:443
                 * Production server: api.push.apple.com:443
                 *
                 * api.development.push.apple.com这个域名苹果官网现在找不到了
                 */
                String SANDBOX_APNS_HOST = "api.sandbox.push.apple.com";
//                /Users/liguoxin/Desktop/p12/deve_push.p12
//                 /Users/liguoxin/Desktop/p12/distri_push.p12
                //四个线程
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
//                注销掉  * api.development.push.apple.com这个域名苹果官网现在找不到了
//                apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)

                apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                        .setClientCredentials(new File("/Users/as/Desktop/apnstuisong.p12"),"123456")
                        .setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup).build();

//                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
//                apnsClient = new ApnsClientBuilder().setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
//                        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File("/Users/liguoxin/Desktop/p12/deve_push.p8"),
//                                "我的temid", "我的keyid"))
//                        .setConcurrentConnections(4).setEventLoopGroup(eventLoopGroup).build();
            } catch (Exception e) {
//                log.error("ios get pushy apns client failed!");
                System.out.println("ios get pushy apns client failed!");
                e.printStackTrace();
            }
        }

        return apnsClient;

    }
}
