package com.booqueen.partner.room;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.booqueen.partner.common.S3Service;
import com.booqueen.partner.hotel.HotelImageVO;
import com.booqueen.partner.hotel.HotelService;
import com.booqueen.partner.hotel.HotelServiceVO;
import com.booqueen.partner.hotel.HotelVO;

@Controller
public class RoomController {

	@Autowired
	private S3Service s3Service;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RoomService roomService;


	@RequestMapping(value = "/get-service.pdo", method = RequestMethod.GET)
	public String getFacilitiesPage(@ModelAttribute("hotel") HotelVO hotel,
			@ModelAttribute("service") HotelServiceVO service, @ModelAttribute("basic") FacilitiesBasicVO basic,
			@ModelAttribute("bath") FacilitiesBathVO bath, @ModelAttribute("media") FacilitiesMediaVO media,
			@ModelAttribute("view") FacilitiesViewVO view, @ModelAttribute("access") FacilitiesAccessVO access,
			@ModelAttribute("room_service") FacilitiesServiceVO room_service, Model model, HttpSession session) {
		try {
			hotel = hotelService.getHotelByMemberEmail(session.getAttribute("email").toString());
			if (hotel != null) {
				service = hotelService.getHotelServiceByHotelSerial(hotel.getSerialnumber());
				basic = roomService.getBasicInfoByHotelSerial(hotel.getSerialnumber());
				bath = roomService.getBathInfoByHotelSerial(hotel.getSerialnumber());
				media = roomService.getMediaInfoByHotelSerial(hotel.getSerialnumber());
				view = roomService.getViewInfoByHotelSerial(hotel.getSerialnumber());
				access = roomService.getAccessInfoByHotelSerial(hotel.getSerialnumber());
				room_service = roomService.getServiceInfoByHotelSerial(hotel.getSerialnumber());
				model.addAttribute("service", service);
				model.addAttribute("basic", basic);
				model.addAttribute("bath", bath);
				model.addAttribute("media", media);
				model.addAttribute("view", view);
				model.addAttribute("access", access);
				model.addAttribute("room_service", room_service);
				model.addAttribute("hotel", hotel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "set-service";
	}

	@RequestMapping(value = "/update-picture.pdo", method = RequestMethod.GET)
	public String getUpdatePictureView(HotelVO hotel, Model model, HttpSession session, UpdateImageVO img) {
		try {
			hotel = hotelService.getHotelByMemberEmail(session.getAttribute("email").toString());
			if (hotel != null) {
				HotelImageVO image = roomService.selectImageBySerial(hotel.getSerialnumber());
				List<UpdateImageVO> room_image = roomService.selectRoomImageBySerial(hotel.getSerialnumber());
				List<UpdateImageVO> type = roomService.selectTypeBySerial(hotel.getSerialnumber());
//				UpdateImageVO room_id = roomService.selectRoom_idBySerial(img.getSerialnumber());
				
//				System.out.println(room_id);
				System.out.println(type.toString());
				System.out.println(image.toString());
				System.out.println(room_image.toString());
				model.addAttribute("hotel", hotel);
				model.addAttribute("image", image);
				model.addAttribute("room_image", room_image);
				model.addAttribute("type", type);
//				model.addAttribute("room_id", room_id);

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "update-picture";
	}

	@RequestMapping(value = "/update-picture.pdo", method = RequestMethod.POST)
	public String updatepicturePost(HotelVO hotel, MultipartFile[] uploadFile, HttpSession session, Model model, UpdateImageVO img, HotelImageVO vo, @RequestParam("room_id") int room_id) {
		String uploadFolder = "C:\\upload";
		File uploadPath = new File(uploadFolder);
		int count = 0;
		
		if (uploadPath.exists() == false) {
			uploadPath.mkdir();
		}
		
		hotel = hotelService.getHotelByMemberEmail((String) session.getAttribute("email"));
		int length = uploadFile.length;
		System.out.println("업로드파일 : " + length);
		for (MultipartFile multipartFile : uploadFile) {
			
			
			String uploadFileName = multipartFile.getOriginalFilename();
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				count++;

				if (count != 0) {
					try {
						InputStream is = multipartFile.getInputStream();
						String contentType = multipartFile.getContentType();
						long contentLength = multipartFile.getSize();
						s3Service.upload(is, "hotel/" + hotel.getSerialnumber() + "/" + uploadFileName, contentType, contentLength);
						System.out.println("https://booqueen.s3.ap-northeast-2.amazonaws.com/hotel/" + uploadFileName);
					
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("1");

					img.setSerialnumber(hotel.getSerialnumber());
					img.setFile_name(uploadFileName);
//					img.setRoom_id(img.getRoom_id());
					img.setFile_url("https://booqueen.s3.ap-northeast-2.amazonaws.com/hotel/" + img.getSerialnumber() + "/" + uploadFileName);
					roomService.insertRoomImage(img);
					
					
//					System.out.println(room_id+"room_id");			
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
//		model.addAttribute("room_id", room_id);
		model.addAttribute("hotel", hotel);
		model.addAttribute("image",img);
		return "update-picture";
	}

	@RequestMapping(value = "/addHotel.pdo", method = RequestMethod.GET)
	public String addRoom(Model model, HotelVO hotel, HttpSession session) {
		try {
			hotel = hotelService.getHotelByMemberEmail(session.getAttribute("email").toString());
			if (hotel != null) {
				model.addAttribute("hotel", hotel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	@RequestMapping(value = "/roomlist.pdo", method = RequestMethod.GET)
	public String roomList(Model model, HotelVO hotel, HttpSession session) {
		RoomVO room = null;
		try {
			hotel = hotelService.getHotelByMemberEmail(session.getAttribute("email").toString());
			if (hotel != null) {
				room = roomService.getRoomByHotelSerial(hotel.getSerialnumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("room", room);
		return "price";
	}

	@RequestMapping(value = "/remove-picture.pdo", method = RequestMethod.POST)
	public String removePicture(HotelVO hotel, Model model, HttpSession session, MultipartFile deleteFile) {
		try {
			hotel = hotelService.getHotelByMemberEmail(session.getAttribute("email").toString());
			if (hotel != null) {
				List<HotelImageVO> image = roomService.deleteImageBySerial(hotel.getSerialnumber());
//			System.out.println(image.toString());
				model.addAttribute("hotel", hotel);
				model.addAttribute("image", image);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login";
	}

//	@RequestMapping(value = "/remove-picture.pdo", method = RequestMethod.POST)
//	public String removePicture(HotelVO hotel, Model model, @RequestParam("serialnumber") int serialnumber,
//			@RequestParam("file_name") String file_name) {
//		System.out.println("호텔명: " + file_name);
//
//		String key = "hotel/" + serialnumber + "/" + file_name;
//		System.out.println("s3경로 : " + key);
//		try {
////			s3Service.delete(file_name);
////			System.out.println(file_name);
//			s3Service.delete(key);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "login";
//	}

}