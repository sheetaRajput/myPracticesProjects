package com.leavemanagement.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.leavemanagement.config.CommonConfig;
import com.leavemanagement.controller.LeaveController;
import com.leavemanagement.dectionary.LeaveStatus;
import com.leavemanagement.entity.ApplyLeave;
import com.leavemanagement.entity.Employee;
import com.leavemanagement.entity.FinalResponse;
import com.leavemanagement.entity.LeaveInfoEmployee;
import com.leavemanagement.entity.LeaveInformation;
import com.leavemanagement.error.LeaveNotFoundException;
import com.leavemanagement.repository.EmpRepo;
import com.leavemanagement.repository.LeaveInformationRepo;
import com.leavemanagement.repository.LeaveRepo;
import com.leavemanagement.utils.CommonUtils;

@Service
public class LeaveService {

	@Autowired
	private LeaveRepo leaveRepo;
	private final Logger LOGGER = LoggerFactory.getLogger(LeaveController.class);

	@Autowired
	private CommonConfig config;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private EmpRepo repo;

	@Autowired
	private LeaveInformationRepo lRepo;

	@Autowired
	private LeaveEmployeeService leService;

	/**
	 * @RequestBody leave
	 * @return FinalResponse with leaveStatus, sendVerification email, DateFormats
	 *         dd-MM-yyyy HH:mm:ss with 24 Hours
	 */
	public ResponseEntity<FinalResponse> applyLeave(ApplyLeave leave, String siteURL) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("applyLeave[START] input parameters:" + leave.toString());
		FinalResponse validation = validation(leave);
		try {
			if (validation.getMessage() != null) {
				return new ResponseEntity<FinalResponse>(validation, HttpStatus.NO_CONTENT);
			} else {
				String differenceHours = differenceOfHours(leave.getToDate(), leave.getFromDate());
				int differenceOfDay = differenceOfDay(leave.getToDate(), leave.getFromDate());
				Employee empl = repo.findByNameLike(leave.getEmpName());
				leave.setEmpId(empl.getEmpId());
				leave.setNoOfHours(differenceHours);
				leave.setLeaveStatus(LeaveStatus.PENDING.getName());
				leave.setNoOfDays(differenceOfDay);
//				sendVerificationEmail(leave, siteURL);
				String typ = leave.getLeaveType();
				LeaveInfoEmployee lobj = leService.getEmployeeLeaveDetails(leave.getEmpId());
				switch (typ) {
				case "Earned Leave":
					if ((lobj.getEarnedLeave() - differenceOfDay) < 0) {
						finalResponse.setMessage(config.getLeaveNotExists());
						finalResponse.setStatus(false);
						return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_ACCEPTABLE);
					}
					break;
				case "Covid Leave":
					if ((lobj.getCovidLeave() - differenceOfDay) <= 0) {
						finalResponse.setMessage(config.getLeaveNotExists());
						finalResponse.setStatus(false);
						return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_ACCEPTABLE);
					}
					break;
				case "Incidental Leave":
					if ((lobj.getInicidentalLeave() - differenceOfDay) <= 0) {
						finalResponse.setMessage(config.getLeaveNotExists());
						finalResponse.setStatus(false);
						return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_ACCEPTABLE);
					}
					break;
				case "Leave Without Pay":
					if ((lobj.getLeaveWithoutPay() - differenceOfDay) <= 0) {
						finalResponse.setMessage(config.getLeaveNotExists());
						finalResponse.setStatus(false);
						return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_ACCEPTABLE);
					}
					break;
				case "Short Leave":
					if ((lobj.getShortLeave() - differenceOfDay) <= 0) {
						finalResponse.setMessage(config.getLeaveNotExists());
						finalResponse.setStatus(false);
						return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_ACCEPTABLE);
					}
					break;
				case "Work from Home":
					if ((lobj.getWfh() - differenceOfDay) < -1) {
						finalResponse.setMessage(config.getLeaveNotExists());
						finalResponse.setStatus(false);
						return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_ACCEPTABLE);
					}
					break;
				}
				ApplyLeave save = leaveRepo.save(leave);
				finalResponse.setData(save);
				finalResponse.setMessage(config.getLeaveSuccess());
				finalResponse.setStatus(true);
				LOGGER.debug("applyLeave[END] input parameters:" + leave.toString());
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.CREATED);
			}
		} catch (ParseException e) {
			LOGGER.error("Date And Time Formate wrong!" + e.getMessage());
			finalResponse.setMessage("Date And Time Formate wrong!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (MailAuthenticationException e) {
			LOGGER.error("failed to connect, no password specified! " + e.getMessage());
			finalResponse.setMessage("failed to connect, no password specified!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(config.getLeaveCreateExce() + e.getMessage());
			finalResponse.setStatus(Boolean.FALSE);
			finalResponse.setMessage(config.getLeaveCreateExce());
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String differenceOfHours(String start_date, String end_date) throws ParseException {
		LOGGER.debug("differenceOfHours[START] ");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date d1 = sdf.parse(start_date);
		Date d2 = sdf.parse(end_date);
		long difference_In_Time = d2.getTime() - d1.getTime();
		try {
			long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

			long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
			long days = TimeUnit.MILLISECONDS.toDays(difference_In_Time);
			if (days == 0) {
				String h = Long.toString(difference_In_Hours);
				String m = Long.toString(difference_In_Minutes);
				LOGGER.debug("differenceOfHours[END]");
				return "0" + h + " : " + m + " Hours";
			} else {
				String h = Long.toString(difference_In_Hours);
				String m = Long.toString(difference_In_Minutes);
				LOGGER.debug("differenceOfHours[END]");
				return "0" + h + " : " + m + " Hours";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(config.getLeaveCreateExce() + e.getMessage());
			return e.getMessage();
		}

	}

	private int differenceOfDay(String start_date, String end_date) throws ParseException {
		LOGGER.debug("differenceOfDay[START] ");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date d1 = sdf.parse(start_date);
		Date d2 = sdf.parse(end_date);
		Date now = new Date();
		int days = 0;

		if (d1.before(now) || d2.before(d1)) {
			LOGGER.error("differenceOfDay[Exception] Date and time cannot be greater than today date");
			throw new DateTimeException("Date and time cannot be greater than today date");
		} else {
			long difference_In_Time = d2.getTime() - d1.getTime();
			try {
				days = (int) TimeUnit.MILLISECONDS.toDays(difference_In_Time);
				String startsub = start_date.substring(0, 10);
				String endsub = end_date.substring(0, 10);
				if (d1.equals(d2) || startsub.equals(endsub)) {
					LOGGER.debug("differenceOfDay[END]");
					return days + 1;
				} else {
					LOGGER.debug("differenceOfDay[END]");
					return days + config.getEndDay();
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(config.getLeaveCreateExce() + e.getMessage());
				throw new ParseException("Throws Parse Exception", days);
			}
		}
	}

	private FinalResponse validation(ApplyLeave leave) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.debug("validation[START] ");
		if (CommonUtils.isNullOrEmpty(leave.getReason())) {
			finalResponse.setMessage("Leave Description is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getLeaveType())) {
			finalResponse.setMessage("Leave Type is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getFromDate())) {
			finalResponse.setMessage("from Date is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getToDate())) {
			finalResponse.setMessage("to Date is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getAlterNativeResource())) {
			finalResponse.setMessage("Alter native resourse name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getDepartment())) {
			finalResponse.setMessage("Department is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getEmpName())) {
			finalResponse.setMessage("Employee Name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		} else if (CommonUtils.isNullOrEmpty(leave.getTeamLeader())) {
			finalResponse.setMessage("Team Leader Name is null !");
			finalResponse.setStatus(false);
			return finalResponse;
		}
		LOGGER.debug("validation[END] ");
		return finalResponse;
	}

	@SuppressWarnings("unused")
	private void sendVerificationEmail(ApplyLeave leave, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		LOGGER.info("Email sending[Start] Got the result");
		Employee teamLeader = repo.findByNameLike(leave.getTeamLeader());
		Employee alterNativeResourse = repo.findByNameLike(leave.getAlterNativeResource());
		String toAddress = teamLeader.getEmail();
		Employee employee = repo.findByNameLike(leave.getEmpName());
		String sender = employee.getEmail();
//		String senderName = leave.getEmpName();
		String subject = "Leave for " + leave.getLeaveType();
		String[] cc = { alterNativeResourse.getEmail(), config.getHeadQuraterMail() };
		String content = "Dear [[name]] sir,<br><br>" + leave.getReason() + "<br>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(new InternetAddress(sender));
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setCc(cc);
		content = content.replace("[[name]]", leave.getTeamLeader());
		helper.setText(content, true);
		mailSender.send(message);
		LOGGER.debug("Email sending[END]");
	}

	/**
	 * @return list of leave with paging and sorting
	 * @Param offSet
	 */
	public ResponseEntity<Page<ApplyLeave>> fetchLeaveByList(int offset, int size, String field) {
		LOGGER.debug("fetchLeaveByList[START-END] ");
		Page<ApplyLeave> findAll = leaveRepo
				.findAll(PageRequest.of(offset, size).withSort(Sort.by(Direction.DESC, field)));
		if (findAll.getSize() <= 0) {
			return new ResponseEntity<Page<ApplyLeave>>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Page<ApplyLeave>>(findAll, HttpStatus.OK);
		}
	}

	/**
	 * @return leave with id
	 * @PathVariable id
	 */
	public ResponseEntity<FinalResponse> fetchLeaveById(Long id) throws LeaveNotFoundException {
		FinalResponse finalResponse = new FinalResponse();
		Optional<ApplyLeave> leave = leaveRepo.findById(id);
		LOGGER.debug("fetchLeaveById[START] ");
		if (!leave.isPresent()) {
			finalResponse.setMessage("Leave is not found");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.NOT_FOUND);
		} else {
			LOGGER.debug("fetchLeaveById[END] ");
			finalResponse.setData(leave);
			finalResponse.setMessage("Leave successfully getting");
			finalResponse.setStatus(true);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
		}
	}

	/**
	 * @return list if leave with empId
	 * @PathVariable empId
	 */
	public ResponseEntity<FinalResponse> getLeaveByEmpId(int empId) {
		// TODO Auto-generated method stub
		LOGGER.debug(" getLeaveByEmpId[START-END] ");
		List<ApplyLeave> record = leaveRepo.findApplyLeavesByEmpId(empId);
		Employee findByEmpId = repo.findEmployeeByEmpId(empId);
		FinalResponse finalResponse = new FinalResponse();
		if (findByEmpId == null) {
			finalResponse.setMessage("Employee id is not exists");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.BAD_REQUEST);
		} else {
			try {
				if (record.size() <= 0) {
					finalResponse.setMessage("record is not exists");
					finalResponse.setStatus(false);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.BAD_REQUEST);
				} else {
					finalResponse.setMessage("Successfully");
					finalResponse.setStatus(true);
					finalResponse.setData(record);
					return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
				}
			} catch (Exception e) {
				finalResponse.setMessage(e.getMessage());
				finalResponse.setStatus(false);
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	public ApplyLeave getLeaveByLeaveId(long leaveid) {
		return leaveRepo.findById(leaveid).get();
	}

	/**
	 * @Path leave Id
	 * @path action - approved or reject
	 * @return finalResponse with leave status changes
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public ResponseEntity<FinalResponse> getLeaveStatusUpdate(String action, long leaveid) {
		FinalResponse finalResponse = new FinalResponse();
		LOGGER.info("getLeaveStatusUpdate[START]");
		if (!leaveRepo.existsById(leaveid)) {
			finalResponse.setMessage("Id is not exists!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(HttpStatus.NOT_FOUND);
		}
		try {
			ApplyLeave leave = leaveRepo.findById(leaveid).get();
			if (action.equalsIgnoreCase("approved")) {
				int i = leave.getEmpId();
				String typ = leave.getLeaveType();
				LeaveInfoEmployee lobj = leService.getEmployeeLeaveDetails(i);
				leave.setLeaveStatus(LeaveStatus.APPROVED.getName());
				switch (typ) {
				case "Earned Leave":
					lobj.setEarnedLeave(lobj.getEarnedLeave() - leave.getNoOfDays());
					break;
				case "Covid Leave":
					lobj.setCovidLeave(lobj.getCovidLeave() - leave.getNoOfDays());
					break;
				case "Incidental Leave":
					lobj.setInicidentalLeave(lobj.getInicidentalLeave() - leave.getNoOfDays());
					break;
				case "Leave Without Pay":
					lobj.setLeaveWithoutPay(lobj.getLeaveWithoutPay() - leave.getNoOfDays());
					break;
				case "Short Leave":
					lobj.setShortLeave(lobj.getShortLeave() - leave.getNoOfDays());
					break;
				case "Work from Home":
					lobj.setWfh(lobj.getWfh() - leave.getNoOfDays());
					break;
				}

				leService.saveLeaveInfo(lobj);
				ApplyLeave update = leaveRepo.save(leave);
//				sendApproveAndRejectEmail(leave);
				finalResponse.setMessage("Leave is Approved...");
				finalResponse.setStatus(true);
				finalResponse.setData(update);
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
			} else if (action.equalsIgnoreCase("rejected")) {
				leave.setLeaveStatus(LeaveStatus.REJECTED.getName());
//				sendApproveAndRejectEmail(leave);
				ApplyLeave update = leaveRepo.save(leave);
				finalResponse.setMessage("Leave is Rejected!");
				finalResponse.setStatus(true);
				finalResponse.setData(update);
				return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.OK);
			}
		} catch (MailAuthenticationException e) {
			finalResponse.setMessage("failed to connect, no password specified!");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			// TODO: handle exception
			finalResponse.setMessage("Leave Status facing exception");
			finalResponse.setStatus(false);
			return new ResponseEntity<FinalResponse>(finalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<FinalResponse>(HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	private void sendApproveAndRejectEmail(ApplyLeave leave) throws MessagingException {
		LOGGER.info("Email sending[Start] for Approve And Reject Got the result");
		Employee teamLeader = repo.findByNameLike(leave.getTeamLeader());
		String teamLeadercc = teamLeader.getEmail();
		Employee employee = repo.findByNameLike(leave.getEmpName());
		String from = "no-reply@anviam.com";
		String subject = "HRM leave is " + leave.getLeaveStatus();
		String[] cc = { teamLeadercc, config.getHeadQuraterMail() };
		String content = "Dear [[name]] sir,<br><br>" + "Your " + leave.getLeaveType() + " Leave is "
				+ leave.getLeaveStatus() + "<br>";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(new InternetAddress(from));
		helper.setTo(employee.getEmail());
		helper.setSubject(subject);
		helper.setCc(cc);
		content = content.replace("[[name]]", employee.getName());
		helper.setText(content, true);
		System.out.println(helper);
		System.out.println(message);
		mailSender.send(message);
		LOGGER.info("Email sending[END] for Approve And Reject");
	}

	public LeaveInformation getLeaveDetails() {
		LOGGER.info("getLeaveDetails[START]");
		try {
			List<LeaveInformation> l = (List<LeaveInformation>) lRepo.findAll();
			return l.get(l.size() - 1);
		} catch (Exception e) {
			return null;
		}

	}

	public LeaveInformation saveLeaveDetails(LeaveInformation lI) {
		LOGGER.info("saveLeaveDetails[END]");
		LeaveInformation leave = lRepo.save(lI);
		return leave;
	}
}
