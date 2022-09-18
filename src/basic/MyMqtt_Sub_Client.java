package basic;

import org.eclipse.paho.client.mqttv3.*;

/**
 * MQTT 클라이언트 작성 - broker에 메시지를 전달받기 위해 구독신청을 하고 대기라는 객체
 * 1. MqttCallback 인터페이스 상속
 * 2. MqttCallback 인터페이스의 abstact 메서드를 오버라이딩 해야 함
 */
public class MyMqtt_Sub_Client implements MqttCallback {

    // broker와 통신하는 역할을 담당
    // MQTT 통신에서 Publisher와 Subscriber의 역할을 하기 위한 기능을 가지고 있는 객체
    private MqttClient mqttClient;

    // Mqtt 프로토콜을 이용해서 broker에 연결하면서 연걸정보를 설정할 수 있는 객체
    private MqttConnectOptions mqttOptiom;

    // clientId는 broker가 클라이언트를 식별하기 위한 문자열 - 아이디는 고유성을 가짐
    public MyMqtt_Sub_Client init(String server, String clientId) {
        try {

            mqttOptiom = new MqttConnectOptions();
            mqttOptiom.setCleanSession(true);
            mqttOptiom.setKeepAliveInterval(30);

            // broker에 subscribe하기 위한 클라이언트객체 생성
            mqttClient = new MqttClient(server, clientId);

            // 클라이언트 객체에 MqttCallback을 등록 - 구독 신청 후 적절한 시점에 처리하고 싶은 기능 구현하고
            // 메소드가 자동을 그 시점에 호출되도록 할 수 있다.
            mqttClient.setCallback(this);
            mqttClient.connect(mqttOptiom);

        } catch (MqttException e) {
            e.printStackTrace();
        }

        return this;
    };

    /**
     * 커넥션이 종료되면 호출 - 통신오류로 연결이 끊어지는 경우 호출
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {

    }

    /**
     * 메시지의 배달이 완료되면 호출
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    /**
     * 메시지가 도착하면 호출되는 메소드
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("================== 메시지 도착 ==================");
        System.out.println("message = " + message);
        System.out.println("topic = " + topic + ", id = "
                + message.getId() + ", payload:" + new String(message.getPayload()));
    }

    // 구독 신청
    public boolean subscribe(String topic) {
        boolean result = true;
        try {
            if (topic != null) {
                // topic과 Qos를 전달
                // Qos는 메시지가 도착하기 위한 품질의 값을 설정 - 서비스 품질 관리
                // 0, 1, 2를 설정
                mqttClient.subscribe(topic, 0);
            }
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public static void main(String[] args) {
        MyMqtt_Sub_Client subObj = new MyMqtt_Sub_Client();
        subObj.init("tcp://아이피 주소:1883", "myid2").subscribe("iot");
    }

}
