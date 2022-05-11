
package com.eastnets.textbreak.readers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eastnets.textbreak.bean.SourceData;

@Service
public class MqReader extends DataReader {

	/*
	 * @Autowired private JmsTemplate jmsTemplate;
	 * 
	 * private Destination destination; private String queueName; BlockingQueue<SourceData> apachecListerQue = new LinkedBlockingQueue<>();
	 */

	/**
	 * 
	 * -In synchronous mode, the thread that calls the receive method will not return, but waits indefinitely to pick the message. I strongly recommend not using this mode unless you have a strong
	 * case. Should you have no alternatives other than using synchronous receive method, use it by setting a value on timeout. -In the asynchronous mode, the client will let the provider know that it
	 * would be interested in receiving the messages from a specific destination or set of destinations. When a message arrives at the given destination, the provider checks the list of clients
	 * interested in the message and will send the message to that list of clients. Receiving Messages Synchronously so you want call receive() immediately
	 * 
	 */
	@Override
	public List<SourceData> readMessages() {
		/*
		 * TextMessage textMessage; textMessage = (TextMessage)jmsTemplate.receive(); try { System.out.println(textMessage.getText()); } catch (JMSException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * 
		 * sendMesg(new TextBreakMQBean("HOLA")); TextBreakMQBean textBreakMQBean = (TextBreakMQBean) jmsTemplate.receiveAndConvert();
		 */
		return null;
	}
	/*
	 * 
	 * @Override public void onMessage(Message message) { TextMessage textMessage = null; try { SourceData sourceData=new SourceData(); textMessage = (TextMessage) message;
	 * sourceData.setTextDataBlock(textMessage.getText()); apachecListerQue.put(sourceData); } catch(Exception e){ } }
	 * 
	 * 
	 * private void sendMesg(final TextBreakMQBean textBreakMQBean) { // TODO Auto-generated method stub
	 * 
	 * jmsTemplate.convertAndSend(textBreakMQBean);
	 * 
	 * 
	 * jmsTemplate.send(new MessageCreator() {
	 * 
	 * @Override public Message createMessage(Session session) throws JMSException {
	 * 
	 * ObjectMessage message = session.createObjectMessage( textBreakMQBean ); return message; } });
	 * 
	 * 
	 * }
	 * 
	 * @Override public List<SourceData> restoreMessages() { // TODO Auto-generated method stub return null; }
	 * 
	 * public JmsTemplate getJmsTemplate() { return jmsTemplate; }
	 * 
	 * public void setJmsTemplate(JmsTemplate jmsTemplate) { this.jmsTemplate = jmsTemplate; }
	 * 
	 * public Destination getDestination() { return destination; }
	 * 
	 * public void setDestination(Destination destination) { this.destination = destination; }
	 * 
	 * public String getQueueName() { return queueName; }
	 * 
	 * public void setQueueName(String queueName) { this.queueName = queueName; }
	 */

	@Override
	public List<SourceData> restoreMessages() {
		// TODO Auto-generated method stub
		return null;
	}
}
