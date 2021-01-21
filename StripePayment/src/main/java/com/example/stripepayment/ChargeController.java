package com.example.stripepayment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.stripepayment.ChargeRequest.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import lombok.extern.java.Log;

@Log
@Controller
public class ChargeController {
	@Autowired
	StripeService paymentsService;

	@GetMapping("/iam")
	public String get() {
		return "start";
	}
	
	@PostMapping("/charge")
	public String charge(ChargeRequest chargeRequest, Model model) throws StripeException {
		chargeRequest.setDescription("Example charge");
		chargeRequest.setCurrency(Currency.EUR);
		Charge charge = paymentsService.charge(chargeRequest);
		System.out.println("charge "+charge);
		model.addAttribute("id", charge.getId());
		model.addAttribute("status", charge.getStatus());
		model.addAttribute("chargeId", charge.getId());
		model.addAttribute("balance_transaction", charge.getBalanceTransaction());
		return "result";
	}

	@ExceptionHandler(StripeException.class)
	public String handleError(Model model, StripeException ex) {
		model.addAttribute("error", ex.getMessage());
		return "result";
	}
}
