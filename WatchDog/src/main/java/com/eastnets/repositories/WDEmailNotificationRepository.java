package com.eastnets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eastnets.entities.WDEmailNotification;
import com.eastnets.watchdog.resultbeans.EventEmailNotification;
import com.eastnets.watchdog.resultbeans.MessageEmailNotification;

public interface WDEmailNotificationRepository extends JpaRepository<WDEmailNotification, Integer> {

	@Query("SELECT new com.eastnets.watchdog.resultbeans.MessageEmailNotification(w.id,w.description, w.username, w.aid, w.umidl, w.umidh, w.processStatus"
			+ ",w.messageType, m.mesgCreaDateTime,m.mesgRelTrnRef,m.mesgUumid,m.mesgUumidSuffix,m.mesgSenderX1,m.xReceiverX1,"
			+ "m.mesgSubFormat,m.mesgNature,m.mesgTrnRef,m.xFinValueDate, m.xFinAmount, m.xFinCcy,m.text.textDataBlock,m.mesgSyntaxTableVer)" + " from WDEmailNotification w , Mesg m"
			+ " where ((w.processStatus = 0 or w.processStatus = null)  and w.aid = m.id.aid and w.umidl = m.id.umidl and w.umidh = m.id.umidh and w.messageType <> 'EVT')")
	List<MessageEmailNotification> getAvailableMessagesEmailNotifications();

	@Query("SELECT new com.eastnets.watchdog.resultbeans.EventEmailNotification(w.id,w.description,w.username,w.aid,w.umidl,w.umidh,w.processStatus"
			+ ",j.jrnlEventClass,j.jrnlEventNum,j.jrnlEventName,j.jrnlCompName,j.jrnlEventServerity,j.jrnlApplServName," + "j.jrnlOperNickName,j.jrnlHostname,j.jrnlMergedText,j.jrnlDateTime)" + " from WDEmailNotification w, Jrnl j "
			+ "where ( (w.processStatus = 0 or w.processStatus = null) and w.messageType = 'EVT' and w.aid = j.jrnlPK.aid and w.umidl = j.jrnlPK.jrnlRevDateTime and  w.umidh = j.jrnlPK.jrnlSeqNumber)")
	List<EventEmailNotification> getAvailableEventsEmailNotifications();

	@Modifying
	@Query("UPDATE WDEmailNotification w SET w.processStatus = 1 where w.id = :id  ")
	void updateEmailReaderStatus(@Param("id") Integer id);

	@Query("SELECT new com.eastnets.watchdog.resultbeans.MessageEmailNotification(w.id,w.description, w.username, w.aid, w.umidl, w.umidh, w.processStatus"
			+ ",w.messageType, m.mesgCreaDateTime,m.mesgRelTrnRef,m.mesgUumid,m.mesgUumidSuffix,m.mesgSenderX1,m.xReceiverX1," + "m.mesgSubFormat,m.mesgNature,m.mesgTrnRef,m.xFinValueDate, m.xFinAmount,"
			+ " m.xFinCcy,m.text.textDataBlock,m.mesgSyntaxTableVer)" + " from WDEmailNotification w , Mesg m" + " where (w.processStatus = 1 and w.aid = m.id.aid and w.umidl = m.id.umidl and w.umidh = m.id.umidh and w.messageType <> 'EVT')")
	List<MessageEmailNotification> restoreMessageEmailNotifications();

	@Query("SELECT new com.eastnets.watchdog.resultbeans.EventEmailNotification(w.id,w.description,w.username,w.aid,w.umidl,w.umidh,w.processStatus"
			+ ",j.jrnlEventClass,j.jrnlEventNum,j.jrnlEventName,j.jrnlCompName,j.jrnlEventServerity,j.jrnlApplServName," + "j.jrnlOperNickName,j.jrnlHostname,j.jrnlMergedText,j.jrnlDateTime)" + " from WDEmailNotification w, Jrnl j "
			+ "where (w.processStatus = 1 and w.messageType = 'EVT' and w.aid = j.jrnlPK.aid and w.umidl = j.jrnlPK.jrnlRevDateTime and  w.umidh = j.jrnlPK.jrnlSeqNumber)")
	List<EventEmailNotification> restoreEventEmailNotitifications();

	@Modifying
	@Query("UPDATE WDEmailNotification w SET w.processStatus = 2 where w.id = :id  ")
	void updateEmailFailedStatus(@Param("id") Integer id);

	// For Part

	@Query("SELECT new com.eastnets.watchdog.resultbeans.MessageEmailNotification(w.id,w.description, w.username, w.aid, w.umidl, w.umidh, w.processStatus"
			+ ",w.messageType, m.mesgCreaDateTime,m.mesgRelTrnRef,m.mesgUumid,m.mesgUumidSuffix,m.mesgSenderX1,m.xReceiverX1,"
			+ "m.mesgSubFormat,m.mesgNature,m.mesgTrnRef,m.xFinValueDate, m.xFinAmount, m.xFinCcy,m.text.textDataBlock,m.mesgSyntaxTableVer)" + " from WDEmailNotification w , Mesg m"
			+ " where ((w.processStatus = 0 or w.processStatus = null)  and w.aid = m.id.aid and w.umidl = m.id.umidl and w.umidh = m.id.umidh  and w.mesgCreaDateTime = m.mesgCreaDateTime and w.messageType <> 'EVT')")
	List<MessageEmailNotification> getAvailableMessagesEmailNotificationsPart();

	@Query("SELECT new com.eastnets.watchdog.resultbeans.EventEmailNotification(w.id,w.description,w.username,w.aid,w.umidl,w.umidh,w.processStatus"
			+ ",j.jrnlEventClass,j.jrnlEventNum,j.jrnlEventName,j.jrnlCompName,j.jrnlEventServerity,j.jrnlApplServName," + "j.jrnlOperNickName,j.jrnlHostname,j.jrnlMergedText,j.jrnlDateTime)" + " from WDEmailNotification w, Jrnl j "
			+ "where ( (w.processStatus = 0 or w.processStatus = null) and w.messageType = 'EVT' and w.aid = j.jrnlPK.aid and w.umidl = j.jrnlPK.jrnlRevDateTime and  w.umidh = j.jrnlPK.jrnlSeqNumber and w.mesgCreaDateTime = j.jrnlDateTime )")
	List<EventEmailNotification> getAvailableEventsEmailNotificationsPart();

}
