package com.booqueen.partner.review;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.booqueen.partner.hotel.HotelService;
import com.booqueen.partner.hotel.HotelVO;

@Controller
public class ReviewController {
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private ReviewService reviewService;
	
	@RequestMapping(value = "/reviews.pdo", method = RequestMethod.GET)
	public String getReview(HotelVO hotel, ReviewAvgVO avg, Model model, HttpSession session) {
		try {
			hotel = hotelService.getHotelByMemberEmail(session.getAttribute("email").toString());
			avg = reviewService.getReviewAvgByHotelSerial(hotel.getSerialnumber());
			if(hotel != null) {
				model.addAttribute("avg", avg);
				model.addAttribute("hotel", hotel);
				System.out.println(avg.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "reviews";
	}

}
