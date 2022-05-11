package viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.loader.LoaderType;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eastnets.domain.viewer.AppendixExtDetails;
import com.eastnets.domain.viewer.GpiConfirmation;
import com.eastnets.domain.viewer.InstanceExtDetails;
import com.eastnets.domain.viewer.InterventionExtDetails;
import com.eastnets.domain.viewer.IntvAppe;
import com.eastnets.domain.viewer.MessageDetails;
import com.eastnets.domain.viewer.SearchLookups;
import com.eastnets.domain.viewer.SearchResultEntity;
import com.eastnets.service.viewer.ViewerService;
import com.eastnets.test.helper.ViewerUtility;

import enReporting.test.base.BaseAppTest;

//@RunWith(DataDrivenTestRunner.class)
//@DataLoader(filePaths = "Test_Files/Viewer_Test.xls", loaderType = LoaderType.EXCEL)
public class MessageViewerTest extends BaseAppTest {
	// private ViewerService viewerService;
	//
	// class MesgHdr {
	// Integer aid;
	// Integer umidl;
	// Integer umidh;
	// public java.sql.Date mesgCreaDateTime;
	// }
	//
	// MesgHdr getMesgHdr() throws InterruptedException {
	// List<SearchResultEntity> result = null;
	// MesgHdr hdr = new MesgHdr();
	// try {
	// result = viewerService.search(ViewerUtility.buildSearchParam("Swift",
	// "MB20", getLoggedInUser(), 1));
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// hdr.aid = result.get(0).getAid();
	// hdr.umidl = result.get(0).getMesgUmidl();
	// hdr.umidh = result.get(0).getMesgUmidh();
	// hdr.mesgCreaDateTime = result.get(0).getMesgCreaDateTimeOnDB();
	// return hdr;
	// }
	//
	// @Before
	// public void init() {
	// viewerService = (ViewerService) getContext().getBean("viewerService");
	// }

	// @Test
	// public void testExistSearch(@Param(name = "frmtName") String frmtName,
	// @Param(name = "trnRef") String trnRef,
	// @Param(name = "listMax") Integer listMax, @Param(name = "expected")
	// String expected)
	// throws InterruptedException {
	// List<SearchResultEntity> result = null;
	// try {
	// result = viewerService.search(ViewerUtility.buildSearchParam(frmtName,
	// trnRef, getLoggedInUser(), listMax));
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assertNotNull(result);
	// assertEquals(expected, result.get(0).getMesgTrnRef());
	// }
	//
	// @Test
	// public void testNotExistSearch(@Param(name = "frmtName") String frmtName,
	// @Param(name = "trnRef") String trnRef,
	// @Param(name = "expected") String expected) throws InterruptedException {
	// List<SearchResultEntity> result = null;
	// try {
	// result = viewerService.search(ViewerUtility.buildSearchParam(frmtName,
	// trnRef, getLoggedInUser(), 1));
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assert (result.isEmpty());
	// }
	//
	// @Test
	// public void testGetViewerSearchLookups() {
	// SearchLookups searchLookups =
	// this.viewerService.getViewerSearchLookups(getLoggedInUser());
	// assertNotNull(searchLookups);
	// assert (!searchLookups.getContentNature().isEmpty());
	// assert (!searchLookups.getSourceSearchFile().isEmpty());
	// assert (!searchLookups.getSourceAvailableSAA().isEmpty());
	// assert (!searchLookups.getUmidFormat().isEmpty());
	// assert (!searchLookups.getContentNature().isEmpty());
	// assert (!searchLookups.getQueuesAvilable().isEmpty());
	// assert (!searchLookups.getUnitsAvailable().isEmpty());
	// assert (!searchLookups.getQualifierList().isEmpty());
	// assert (!searchLookups.getStatusCodeList().isEmpty());
	// assert (!searchLookups.getReasonCodes().isEmpty());
	// assert (!searchLookups.getStatusList().isEmpty());
	// }
	//
	// @Test
	// public void testCount(@Param(name = "frmtName") String frmtName,
	// @Param(name = "trnRef") String trnRef,
	// @Param(name = "listMax") Integer listMax, @Param(name = "expected")
	// Integer expected)
	// throws InterruptedException {
	// Integer count;
	// count = viewerService
	// .getMessagesCount(ViewerUtility.buildSearchParam(frmtName, trnRef,
	// getLoggedInUser(), listMax));
	// assertNotNull(count);
	// assertEquals(expected, count);
	// }
	//
	// @Test
	// public void testSearchHL(@Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh) {
	// List<SearchResultEntity> result = null;
	// try {
	// result = viewerService.searchHL(getLoggedInUser(), umidl, umidh, 0,
	// getLoggedGroupId(), null);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assertNotNull(result);
	// }
	//
	// @Test
	// public void testMessageDetails(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh) throws Exception {
	// java.sql.Date mesgCreaDateTime = viewerService
	// .searchHL(getLoggedInUser(), umidl, umidh, 0, getLoggedGroupId(),
	// aid).get(0).getMesgCreaDateTimeOnDB();
	// MessageDetails messageDetails = this.viewerService.getMessageDetails(
	// ViewerUtility.buildLaodMsgParam(getLoggedInUser(), aid, umidl, umidh,
	// mesgCreaDateTime));
	// assertNotNull(messageDetails);
	// }
	//
	// @Test
	// public void testForceMessageUpdate() {
	// MesgHdr hdr = null;
	// try {
	// hdr = getMesgHdr();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// viewerService.forceMessageUpdate(getLoggedInUser(), hdr.aid, hdr.umidl,
	// hdr.umidh, hdr.mesgCreaDateTime);
	// assertTrue(true);
	// }
	//
	// @Test
	// public void testGetInstanceDetails(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh) throws Exception {
	// MessageDetails messageDetails = this.viewerService
	// .getMessageDetails(ViewerUtility.buildLaodMsgParam(getLoggedInUser(),
	// aid, umidl, umidh, null));
	// assertNotNull(messageDetails);
	// java.sql.Date mesgCreaDateTime = viewerService
	// .searchHL(getLoggedInUser(), umidl, umidh, 0, getLoggedGroupId(),
	// aid).get(0).getMesgCreaDateTimeOnDB();
	// InstanceExtDetails instDetails =
	// this.viewerService.getInstanceDetails(getLoggedInUser(), aid, umidl,
	// umidh,
	// mesgCreaDateTime, messageDetails.getMesgInstances().get(0).getInstNum(),
	// 0);
	// assertNotNull(instDetails);
	// assert (!instDetails.getInstIntvList().isEmpty());
	// }
	//
	// @Test
	// public void testGetAppendixDetails(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh) throws Exception {
	// List<SearchResultEntity> result = null;
	// try {
	// result = viewerService.searchHL(getLoggedInUser(), umidl, umidh, 0,
	// getLoggedGroupId(), aid);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assertNotNull(result);
	// for (SearchResultEntity r : result) {
	// java.sql.Date mesgCreaDateTime = viewerService
	// .searchHL(getLoggedInUser(), r.getMesgUmidl(), r.getMesgUmidh(), 0,
	// getLoggedGroupId(), r.getAid())
	// .get(0).getMesgCreaDateTimeOnDB();
	// MessageDetails messageDetails =
	// this.viewerService.getMessageDetails(ViewerUtility.buildLaodMsgParam(
	// getLoggedInUser(), r.getAid(), r.getMesgUmidl(), r.getMesgUmidh(),
	// mesgCreaDateTime));
	// assertNotNull(messageDetails);
	// InstanceExtDetails instDetails =
	// this.viewerService.getInstanceDetails(getLoggedInUser(), r.getAid(),
	// r.getMesgUmidl(), r.getMesgUmidh(), mesgCreaDateTime,
	// messageDetails.getMesgInstances().get(0).getInstNum(), 0);
	// assertNotNull(instDetails);
	// assert (!instDetails.getInstIntvList().isEmpty());
	//
	// for (IntvAppe appe : instDetails.getInstIntvList()) {
	// if (!appe.isIntervention()) {
	// AppendixExtDetails appeDetails =
	// this.viewerService.getAppendixDetails(getLoggedInUser(),
	// appe.getAid(), appe.getUmidl(), appe.getUmidh(),
	// messageDetails.getMesgCreaDateTimeOnDB(),
	// appe.getInstNum(), appe.getSeqNbr(), appe.getDate(), 0);
	// assertNotNull(appeDetails);
	// assertEquals(r.getMesgUmidl(), appe.getUmidl());
	// }
	// }
	//
	// }
	// }
	//
	// @Test
	// public void testGetInterventionDetails(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh) throws Exception {
	// List<SearchResultEntity> result = null;
	// try {
	// result = viewerService.searchHL(getLoggedInUser(), umidl, umidh, 0,
	// getLoggedGroupId(), aid);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assertNotNull(result);
	// for (SearchResultEntity r : result) {
	// java.sql.Date mesgCreaDateTime = viewerService
	// .searchHL(getLoggedInUser(), r.getMesgUmidl(), r.getMesgUmidh(), 0,
	// getLoggedGroupId(), r.getAid())
	// .get(0).getMesgCreaDateTimeOnDB();
	// MessageDetails messageDetails =
	// this.viewerService.getMessageDetails(ViewerUtility.buildLaodMsgParam(
	// getLoggedInUser(), r.getAid(), r.getMesgUmidl(), r.getMesgUmidh(),
	// mesgCreaDateTime));
	// assertNotNull(messageDetails);
	// InstanceExtDetails instDetails =
	// this.viewerService.getInstanceDetails(getLoggedInUser(), r.getAid(),
	// r.getMesgUmidl(), r.getMesgUmidh(), mesgCreaDateTime,
	// messageDetails.getMesgInstances().get(0).getInstNum(), 0);
	// assertNotNull(instDetails);
	// assert (!instDetails.getInstIntvList().isEmpty());
	//
	// for (IntvAppe intvAppe : instDetails.getInstIntvList()) {
	// if (intvAppe.isIntervention()) {
	// InterventionExtDetails intvDetails =
	// this.viewerService.getInterventionDetails(getLoggedInUser(),
	// intvAppe.getAid(), intvAppe.getUmidl(), intvAppe.getUmidh(),
	// messageDetails.getMesgCreaDateTimeOnDB(), intvAppe.getInstNum(),
	// intvAppe.getSeqNbr(),
	// intvAppe.getDate(), 0);
	// assertNotNull(intvDetails);
	// assertEquals(r.getMesgUmidl(), intvAppe.getUmidl());
	// }
	// }
	// }
	//
	// }
	//
	// @Test
	// public void testMailMessages(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh, @Param(name = "mailTo") String
	// mailTo,
	// @Param(name = "mailSubject") String mailSubject) throws Exception {
	// List<SearchResultEntity> result = null;
	// try {
	// result = viewerService.searchHL(getLoggedInUser(), umidl, umidh, 0,
	// getLoggedGroupId(), aid);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assertNotNull(result);
	// assert (!result.isEmpty());
	// this.viewerService.mailMessages(getLoggedInUser(), mailSubject, mailTo,
	// result, true, true, 0);
	// }
	//
	// @Test
	// public void testGetMessageExpandedText(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh) throws Exception {
	// java.sql.Date mesgCreaDateTime = viewerService
	// .searchHL(getLoggedInUser(), umidl, umidh, 0, getLoggedGroupId(),
	// aid).get(0).getMesgCreaDateTimeOnDB();
	// MessageDetails messageDetails = this.viewerService.getMessageDetails(
	// ViewerUtility.buildLaodMsgParam(getLoggedInUser(), aid, umidl, umidh,
	// mesgCreaDateTime));
	// assertNotNull(messageDetails);
	// String result = "";
	// try {
	// result = this.viewerService.getMessageExpandedText(getLoggedInUser(),
	// messageDetails, null, null);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// assert (!result.isEmpty());
	// }
	//
	// @Test
	// public void testPrintMessages(@Param(name = "frmtName") String frmtName,
	// @Param(name = "trnRef") String trnRef,
	// @Param(name = "listMax") Integer listMax) throws Exception {
	// List<SearchResultEntity> result = null;
	// result = viewerService.search(ViewerUtility.buildSearchParam(frmtName,
	// trnRef, getLoggedInUser(), listMax));
	// assertNotNull(result);
	// String printVal = this.viewerService.printMessages(getLoggedInUser(),
	// result, true, true, 0);
	// assert (!printVal.isEmpty());
	// }
	//
	// @Test
	// public void testExportJRE(@Param(name = "frmtName") String frmtName,
	// @Param(name = "trnRef") String trnRef,
	// @Param(name = "listMax") Integer listMax) throws Exception {
	// List<SearchResultEntity> result = null;
	// result = viewerService.search(ViewerUtility.buildSearchParam(frmtName,
	// trnRef, getLoggedInUser(), listMax));
	// assertNotNull(result);
	// String printVal = null;
	// printVal = this.viewerService.exportRJE(getLoggedInUser(), result, true,
	// 0);
	// assert (!printVal.isEmpty());
	// }
	//
	// @Test
	// public void testIsFieldTagValid(@Param(name = "fieldTag") String
	// fieldTag) {
	// boolean returnVal = this.viewerService.isFieldTagValid(getLoggedInUser(),
	// fieldTag);
	// assert (returnVal == true);
	// }
	//
	// @Test
	// public void testIsNotFieldTagValid(@Param(name = "fieldTag") String
	// fieldTag) {
	// boolean returnVal = this.viewerService.isFieldTagValid(getLoggedInUser(),
	// fieldTag);
	// assert (returnVal == false);
	// }
	//
	// @Test
	// public void testGetGpiConfirmation(@Param(name = "aid") Integer aid,
	// @Param(name = "umidl") Integer umidl,
	// @Param(name = "umidh") Integer umidh, @Param(name =
	// "expectedConfirmation") Integer expectedConfirmation)
	// throws Exception {
	// java.sql.Date mesgCreaDateTime = viewerService
	// .searchHL(getLoggedInUser(), umidl, umidh, 0, getLoggedGroupId(),
	// aid).get(0).getMesgCreaDateTimeOnDB();
	// MessageDetails messageDetails = this.viewerService.getMessageDetails(
	// ViewerUtility.buildLaodMsgParam(getLoggedInUser(), aid, umidl, umidh,
	// mesgCreaDateTime));
	// assertNotNull(messageDetails);
	// List<GpiConfirmation> gpiConfirmationsList =
	// this.viewerService.getGpiAgent(messageDetails.getUetr(), 0, null);
	// assertNotNull(gpiConfirmationsList);
	// Integer size = gpiConfirmationsList.size();
	// assertEquals(expectedConfirmation, size);
	//
	// }

}
