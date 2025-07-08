package com.tld_store.DemoDao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tld_store.DemoDao.imp.TransactionDAOImp;
import com.tld_store.DemoDao.service.EmployeeService;
import com.tld_store.DemoDao.service.TransactionService;

import dao.TransactionDAO;
import dto.Shopper;
import dto.Transaction;
import exception.CustomException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/add_phieuthu")
	public String addPhieuThu(ModelMap model) {
		try {
			Transaction transaction = new Transaction();
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "add");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = "";
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
			model.addAttribute("accountantName", employeeService.getFullNameByCurrentEmail());
		}catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		return "info_detail/phieuthu_detail";
		
	}
	
	@PostMapping("/add_phieuthu")
	public String addPhieuThu(@ModelAttribute Transaction transaction, ModelMap model) {
		try {
			transaction.setAccountantId(employeeService.getCurrentEmployee().getId());
			transactionService.addTransaction(transaction);
			model.addAttribute("message", "Thêm phiếu thu thành công");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			transaction = new Transaction();
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "add");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = "";
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
			model.addAttribute("accountantName", employeeService.getFullNameByCurrentEmail());
		}
		return "info_detail/phieuthu_detail";
		
	}
	
	@GetMapping("/view_phieuthu")
	public String viewPhieuThu(@ModelAttribute("transactionId") int transactionId, ModelMap model) {
		try {
			Transaction transaction = transactionService.getTransactionById(transactionId);
			System.out.println(transaction.getDescription());
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "view");
			model.addAttribute("accountantName", employeeService.getFullNameById(transaction.getAccountantId()));
		}catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		return "info_detail/phieuthu_detail";
		
	}
	
	@GetMapping("/edit_phieuthu")
	public String editPhieuThu(@ModelAttribute("transactionId") int transactionId, ModelMap model) {
		try {
			Transaction transaction = transactionService.getTransactionById(transactionId);
			System.out.println(transaction.getDescription());
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "edit");
			model.addAttribute("accountantName", employeeService.getFullNameById(transaction.getAccountantId()));
		}catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		return "info_detail/phieuthu_detail";
		
	}
	
	@PostMapping("/edit_phieuthu")
	public String editPhieuThu(@ModelAttribute("transaction") Transaction transaction, @RequestParam("transactionId") int transactionId, ModelMap model) {
		try {
			transaction.setTransactionId(transactionId);
			transactionService.updateTransaction(transaction);
			model.addAttribute("message", "Sửa thiếu thu thành công");
		}catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}finally {
			transaction = transactionService.getTransactionById(transactionId);
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "edit");
			model.addAttribute("accountantName", employeeService.getFullNameById(transaction.getAccountantId()));
		}
		return "info_detail/phieuthu_detail";
		
	}
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteShopper(@PathVariable String id) {
    	Transaction transaction = new Transaction();
    	transaction.setTransactionId(Integer.parseInt(id));
    	
    	Map<String, Object> response = new HashMap<>();
        try {
        	transactionService.deleteTransaction(transaction);
            response.put("status", "success");
            response.put("message", "Transaction deleted successfully");
            response.put("id", id);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("id", id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
	
	@GetMapping("receive/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> get10Receives(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Transaction> receives = transactionService.get10TransactionByType("THU", pageNumber, isFinal);
            response.put("users", receives);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }
	
	@GetMapping("receive/search/{input_search}/{pageNum}")
    public ResponseEntity<Map<String, Object>> findTopReceive(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Transaction> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = transactionService.get10TransactionByName(search_name,"THU",pageNum, isFinal);
        	
        	
            response.put("status", "success");
            response.put("isFinal", isFinal.get());
            response.put("users", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
        	System.out.println(e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("users", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GetMapping("/add_phieuchi")
	public String addPhieuChi(ModelMap model) {
		try {
			Transaction transaction = new Transaction();
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "add");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = "";
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
			model.addAttribute("accountantName", employeeService.getFullNameByCurrentEmail());
		}catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		return "info_detail/phieuchi_detail";
		
	}
	
	@PostMapping("/add_phieuchi")
	public String addPhieuChi(@ModelAttribute Transaction transaction, ModelMap model) {
		try {
			transaction.setAccountantId(employeeService.getCurrentEmployee().getId());
			transactionService.addTransaction(transaction);
			model.addAttribute("message", "Thêm phiếu chi thành công");

		} catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}
		finally {
			transaction = new Transaction();
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "add");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = "";
		    if (authentication != null && authentication.isAuthenticated()) {
		        // Lấy username
		        username = authentication.getName();
		    }
			model.addAttribute("accountantName", employeeService.getFullNameByCurrentEmail());
		}
		return "info_detail/phieuchi_detail";
		
	}
	
	@GetMapping("/view_phieuchi")
	public String viewPhieuChi(@ModelAttribute("transactionId") int transactionId, ModelMap model) {
		try {
			Transaction transaction = transactionService.getTransactionById(transactionId);
			System.out.println(transaction.getDescription());
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "view");
			model.addAttribute("accountantName", employeeService.getFullNameById(transaction.getAccountantId()));
		}catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		return "info_detail/phieuchi_detail";
		
	}
	
	@GetMapping("/edit_phieuchi")
	public String editPhieuChi(@ModelAttribute("transactionId") int transactionId, ModelMap model) {
		try {
			Transaction transaction = transactionService.getTransactionById(transactionId);
			System.out.println(transaction.getDescription());
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "edit");
			model.addAttribute("accountantName", employeeService.getFullNameById(transaction.getAccountantId()));
		}catch (CustomException e) {
			System.out.println(e.getMessage());
		}
		return "info_detail/phieuchi_detail";
		
	}
	
	@PostMapping("/edit_phieuchi")
	public String editPhieuChi(@ModelAttribute("transaction") Transaction transaction, @RequestParam("transactionId") int transactionId, ModelMap model) {
		try {
			transaction.setTransactionId(transactionId);
			transactionService.updateTransaction(transaction);
			model.addAttribute("message", "Sửa thiếu chi thành công");
		}catch (CustomException e) {
			model.addAttribute("message", e.getMessage());
		}finally {
			transaction = transactionService.getTransactionById(transactionId);
			model.addAttribute("transaction", transaction);
			model.addAttribute("mode", "edit");
			model.addAttribute("accountantName", employeeService.getFullNameById(transaction.getAccountantId()));
		}
		return "info_detail/phieuchi_detail";
		
	}
	
	@GetMapping("spend/all/{pageNum}")
    public ResponseEntity<Map<String, Object>> get10Spends(@PathVariable String pageNum) {
    	AtomicBoolean isFinal = new AtomicBoolean();
    	Map<String, Object> response = new HashMap<>();
    	int pageNumber = Integer.parseInt(pageNum);
        try {
        	ArrayList<Transaction> receives = transactionService.get10TransactionByType("CHI", pageNumber, isFinal);
            response.put("users", receives);
            response.put("isFinal", isFinal.get());
            return ResponseEntity.ok(response);

		} catch (CustomException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}		
    }
	
	@GetMapping("spend/search/{input_search}/{pageNum}")
    public ResponseEntity<Map<String, Object>> findTopSpend(@PathVariable("input_search") String search_name, @PathVariable("pageNum") int pageNum) {
        //boolean isDeleted = shopperService.deleteShopperById(id);
    	ArrayList<Transaction> arr = new ArrayList<>();
    	
    	Map<String, Object> response = new HashMap<>();
    	AtomicBoolean isFinal = new AtomicBoolean();
        try {
        	arr = transactionService.get10TransactionByName(search_name,"CHI",pageNum, isFinal);
        	
        	
            response.put("status", "success");
            response.put("isFinal", isFinal.get());
            response.put("users", arr);
            return ResponseEntity.ok(response);

		}
        catch (CustomException e) {
        	System.out.println(e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("users", arr);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}	
    	
    }
	
	
	
}
