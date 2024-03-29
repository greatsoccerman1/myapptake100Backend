package com.example.demoController;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import models.AddGroupMememberRequest;
import models.AddGroupMememberResponse;
import models.AddJobRequest;
import models.AddJobResponse;
import models.AddTaskReq;
import models.AddTaskResponse;
import models.DeleteGroupMemberRequest;
import models.DeleteGroupMemberResponse;
import models.EmployeeHistoryRequest;
import models.EmployeeHistoryResponse;
import models.GetGroupMemberInfoRequest;
import models.GetGroupMemberInfoResponse;
import models.GetGroupMembersRequest;
import models.GetGroupMembersResponse;
import models.GetTasksRequest;
import models.JobTasks;
import models.JobTasksModel;
import models.Jobs;
import models.MarkJobCompleteRequest;
import models.RegisterNewAccountRequest;
import models.RegisterNewAccountResponse;
import models.RemoveJobResponse;
import models.RemoveNeed;
import models.UserLoginModel;
import models.productModel;
import services.EmployeeService;
import services.GroupMemeberService;
import services.LoginService;
import services.NeedsController;
import services.JobTaskService;
import services.ProductService;
import services.RefreshService;



@RestController
public class HomeController {
	
	RestTemplate restTemplate; 
	@Autowired
	ProductService productService;
	@Autowired
	LoginService loginService;
	@Autowired
	JobTaskService jobTaskService;
	@Autowired
	GroupMemeberService groupMememberService;
	@Autowired
	NeedsController needsController;
	@Autowired
	RefreshService refreshService;
	@Autowired
	EmployeeService employeeService;

	
	@GetMapping("/products")
	public List<productModel> getAllProducts(){
		return productService.getAllProducts();
	}
	
	@GetMapping("/product/{id}")
	public productModel getProductById(@PathVariable("id")String productId) {
		return productService.getProductById(productId);
	}
	
	@GetMapping("users/{username}/{password}")
	public UserLoginModel getUserLogin(@PathVariable("username")String username, @PathVariable("password")String password) throws ClassNotFoundException {
		return  loginService.getUserLogin(username, password);
	}
	
	@GetMapping("users/jobs/{groupId}/{personId}")
	public Jobs getPeople(@PathVariable("groupId") String groupId, @PathVariable("personId") String personId) throws ClassNotFoundException{
		return jobTaskService.getJobs(groupId, personId);
	}
	
	@PostMapping("users/getTask")
	public JobTasksModel getTask(@RequestBody GetTasksRequest req) throws ClassNotFoundException{
		return jobTaskService.getTask(req);
	}
	
	@PostMapping("users/addGroupMemember")
	public AddGroupMememberResponse addGroupMember(@RequestBody AddGroupMememberRequest req) {
		return groupMememberService.addGroupMember(req);
	}
	
	@GetMapping("users/getGroupMembers/{groupId}")
	public GetGroupMembersResponse getGroupMembers(@PathVariable("groupId") String groupId) {
		return groupMememberService.getGroupMembers(groupId);
	}
	
	@PostMapping("users/deleteGroupMembers")
	public DeleteGroupMemberResponse deleteGroupMembers(@RequestBody DeleteGroupMemberRequest req) throws SQLException {
		return groupMememberService.deleteGroupMembers(req);
	}
	
	@GetMapping("users/removeNeed/{mongoId}")
	public RemoveNeed removeNeed(@PathVariable("mongoId") String mongoId) throws ClassNotFoundException{
		return needsController.removeNeed(mongoId);
	}
	@PostMapping("users/markJobInComplete")
	public MarkJobCompleteResponse markJobInComplete(@RequestBody MarkJobCompleteRequest req) {
		return jobTaskService.markJobInComplete(req);
	}
	
	@PostMapping("users/registerNewAccount")
	public RegisterNewAccountResponse addNewAccount(@RequestBody RegisterNewAccountRequest req) throws ClassNotFoundException{
		return loginService.registerNewAccount(req);
	}
	
	@PostMapping("users/addJob")
	public AddJobResponse addJob(@RequestBody AddJobRequest req) {
		return jobTaskService.addJob(req);
	}
	
	@PostMapping("users/addTask")
	public AddTaskResponse addTask(@RequestBody AddTaskReq req) {
		return jobTaskService.addTask(req);
	}
	
	@PostMapping("users/markJobComplete")
	public MarkJobCompleteResponse markJobComplete(@RequestBody MarkJobCompleteRequest req) {
		return jobTaskService.markJobComplete(req);
	}
	
	@PostMapping("user/refresh")
	public AddTaskResponse refresh() {
		return refreshService.refresh();
	}
	
	@PostMapping("users/removeJob")
	public RemoveJobResponse addJob(@PathVariable("groupId")String groupId, @PathVariable("jobId")String jobId) {
		return jobTaskService.removeJob(groupId, jobId);
	}
	
	@PostMapping("user/getGroupMemberInfo")
	public GetGroupMemberInfoResponse getGroupMemberInfo(@RequestBody GetGroupMemberInfoRequest req) throws SQLException{
		return groupMememberService.getGroupInfo(req);
	}
	
	@PostMapping("user/getEmployeeHistory")
	public EmployeeHistoryResponse getEmployeeHistory(@RequestBody EmployeeHistoryRequest req) throws SQLException{
		return employeeService.getEmployeeHistory(req);
	}
	/* q@GetMapping("users/removeGroupMemember")
	public RemoveGroupMememberResponse removeGroupMember(@RequestBody RemoveGroupMememberRequest req) {
		return addGroupMememberService.addGroupMember(req);
	*/
}
