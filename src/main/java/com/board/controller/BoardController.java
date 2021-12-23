package com.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.domain.BoardDTO;
import com.board.service.BoardService;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	// 메서드

	@GetMapping(value = "/board/write")
	public String openBoardWrite(Model model, @RequestParam(value = "idx", required = false) Long idx) {

		System.out.println(":: CONTROLLER - /board/write ::");

		if (idx == null) {
			model.addAttribute("board", new BoardDTO());
		} else {
			BoardDTO board = boardService.getBoardDetail(idx);
			if (board == null) {
				return "redirect:/board/list";
			}
			model.addAttribute("board", board);
		}

		return "board/write";
	}

	@PostMapping(value = "/board/register")
	public String registerBoard(final BoardDTO params) {

		System.out.println(":: CONTROLLER - /board/register ::");
		System.out.println("params : " + params.toString());

		try {
			boolean isRegistered = boardService.registerBoard(params);
			System.out.println("isRegistered : " + isRegistered);
			if (isRegistered == false) {
				// 게시글 등록에 실패하였다는 메시지를 전달
			}
		} catch (DataAccessException e) {
			// 데이터베이스 처리 과정에 문제가 발생하였다는 메시지를 전달

		} catch (Exception e) {
			// 시스템에 문제가 발생하였다는 메시지를 전달
		}

		return "redirect:/board/list";
	}

	@GetMapping(value = "/board/list")
	public String openBoardList(Model model) {

		System.out.println(":: CONTROLLER - /board/list ::");

		List<BoardDTO> boardList = boardService.getBoardList();
		model.addAttribute("boardList", boardList);
		return "board/list";
	}

	@GetMapping(value = "/board/view")
	public String openBoardDetail(@RequestParam(value = "idx", required = false) Long idx, Model model) {

		System.out.println(":: CONTROLLER - /board/view (idx : " + idx + ") ::");

		if (idx == null) {
			return "redirect:/board/list";
		}

		BoardDTO boardDTO = boardService.getBoardDetail(idx);
		if (boardDTO == null || "Y".equals(boardDTO.getDeleteYn())) {
			return "redirect:/board/list";
		}

		model.addAttribute("board", boardDTO);

		// 조회수 ++
		boolean isPlus = boardService.cntPlus(idx);
		System.out.println("isPlus : " + isPlus);

		return "board/view";

	}

	@PostMapping(value = "board/delete")
	public String deleteBoard(@RequestParam(value = "idx", required = false) Long idx) {

		System.out.println(":: CONTROLLER - /board/delete (idx : " + idx + ") ::");

		if (idx == null) {
			return "redirect:/board/list";
		}

		try {
			boolean isDeleted = boardService.deleteBoard(idx);
			System.out.println("isDeleted : " + isDeleted);
			if (isDeleted == false) {
				System.out.println("삭제 실패");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/board/list";
	}

}
