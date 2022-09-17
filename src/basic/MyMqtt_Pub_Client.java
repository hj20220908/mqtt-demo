package basic;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * MQTT 통신을 통해서 메세지를 broker로 전송하기 위한 객체
 * 1. broker에 접속
 * 2. broker로 메세지를 전송
 */
public class MyMqtt_Pub_Client {

    // MQTT 통신에서 Publisher와 Subscriber의 역할을 하기 위한 기능을 가지고 있는 객체
    private MqttClient client;

    public MyMqtt_Pub_Client() {
        try {
            // broker와 mqtt 통신을 하며 메시지를 전송할 클라이언트 객체를 만들고 접속
            this.client = new MqttClient("tcp://192.168.60.137:1883", "myid");
            client.connect();   // broker 접속
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // 메시지 전송을 위한 메소드
    public boolean send(String topic, String msg) {

        try {
            // broker로 전송할 메시지 생성 - MqttMessage
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());   // 실제 broker로 전송할 메시지를 담음

            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return true;
    }

    // 메시지 전송이 완료되거나 문제가 발생했을 때 종료하는 메서드
    public void close() {
        try {
            if (client != null) {
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        MyMqtt_Pub_Client sender = new MyMqtt_Pub_Client();
        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 1;
                String msg = "";

                while (true) {
                    if (i == 5) {
                        break;
                    } else {

                        if (i % 2 == 1) {
                            msg = "led_on";
                        } else {
                            msg = "led_off";
                        }

                        sender.send("iot", msg);    // 메시지 전송

                        i++;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                sender.close();                  // 작업 완료 후 종료
            }
        }).start();
    }
}
