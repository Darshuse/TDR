package com.eastnets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eastnets.entities.WDEmailNotification;
import com.eastnets.watchdog.custom.results.EventEmailNotification;
import com.eastnets.watchdog.custom.results.MessageEmailNotification;

public interface WDEmailNotificationRepository extends JpaRepository<WDEmailNotification, Integer> {

	@Query("SELECT new com.eastnets.watchdog.custom.results.MessageEmailNotification (w.id,w.description, w.username, w.aid, w.umidl, w.umidh, w.status"
			+ ",w.messageType, m.mesgCreaDateTime,m.mesgRelTrnRef,m.mesgUumid,m.mesgUumidSuffix,m.mesgSenderX1,m.xReceiverX1,"
			+ "m.mesgSubFormat,m.mesgNature,m.mesgTrnRef,m.xFinValueDate, m.xFinAmount, m.xFinCcy)"
			+ " from WDEmailNotification w , Mesg m"
			+ " where ((w.status = 0 or w.status = null)  and w.aid = m.id.aid and w.umidl = m.id.umidl and w.umidh = m.id.umidh and w.messageType <> 'EVT')")
	List<MessageEmailNotification> getAvailableMessagesEmailNotifications();

	@Query("SELECT new com.eastnets.watchdog.custom.results.EventEmailNotification (w.id,w.description,w.username,w.aid,w.umidl,w.umidh,w.status"
			+ ",j.jrnlEventClass,j.jrnlEventNum,j.jrnlEventName,j.jrnlCompName,j.jrnlEventServerity,j.jrnlApplServName,"
			+ "j.jrnlOperNickName,j.jrnlHostname,j.jrnlMergedText)" + " from WDEmailNotification w, Jrnl j "
			+ "where ( (w.status = 0 or w.status = null) and w.messageType = 'EVT' and w.aid = j.jrnlPK.aid and w.umidl = j.jrnlPK.jrnlRevDateTime and  w.umidh = j.jrnlPK.jrnlSeqNumber)")
	List<EventEmailNotification> getAvailableEventsEmailNotifications();

	@Modifying
	@Query("UPDATE WDEmailNotification w SET w.status = 1 where w.id = :id  ")
	void updateEmailReaderStatus(@Param("id") Integer id);

	@Query("SELECT new com.eastnets.watchdog.custom.results.MessageEmailNotification (w.id,w.description, w.username, w.aid, w.umidl, w.umidh, w.status"
			+ ",w.messageType, m.mesgCreaDateTime,m.mesgRelTrnRef,m.mesgUumid,m.mesgUumidSuffix,m.mesgSenderX1,m.xReceiverX1,"
			+ "m.mesgSubFormat,m.mesgNature,m.mesgTrnRef,m.xFinValueDate, m.xFinAmount, m.xFinCcy)"
			+ " from WDEmailNotification w , Mesg m"
			+ " where (w.status = 1 and w.aid = m.id.aid and w.umidl = m.id.umidl and w.umidh = m.id.umidh and w.messageType <> 'EVT')")
	List<MessageEmailNotification> restoreMessageEmailNotifications();

	@Query("SELECT new com.eastnets.watchdog.custom.results.EventEmailNotification (w.id,w.description,w.username,w.aid,w.umidl,w.umidh,w.status"
			+ ",j.jrnlEventClass,j.jrnlEventNum,j.jrnlEventName,j.jrnlCompName,j.jrnlEventServerity,j.jrnlApplServName,"
			+ "j.jrnlOperNickName,j.jrnlHostname,j.jrnlMergedText)" + " from WDEmailNotification w, Jrnl j "
			+ "where (w.status = 1 and w.messageType = 'EVT' and w.aid = j.jrnlPK.aid and w.umidl = j.jrnlPK.jrnlRevDateTime and  w.umidh = j.jrnlPK.jrnlSeqNumber)")
	List<EventEmailNotification> restoreEventEmailNotitifications();

	@Modifying
	@Query("UPDATE WDEmailNotification w SET w.status = 2 where w.id = :id  ")
	void updateEmailFailedStatus(@Param("id") Integer id);

}
