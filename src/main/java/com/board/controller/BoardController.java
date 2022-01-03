package com.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.domain.BoardDTO;
import com.board.paging.Criteria;
import com.board.service.BoardService;
import com.board.util.UiUtils;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@GetMapping(value = "/board/write")
	public String openBoardWrite(Model model, @RequestParam(value = "idx", required = false) Long idx) {

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
	public String registerBoard(final BoardDTO params, Model model) {

		System.out.println("params : " + params.toString());

		try {
			boolean isRegistered = boardService.registerBoard(params);
			System.out.println("isRegistered : " + isRegistered);
			if (isRegistered == false) {
				return UiUtils.showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return UiUtils.showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list", Method.GET, null,
					model);

		} catch (Exception e) {
			return UiUtils.showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list", Method.GET, null, model);
		}

		return UiUtils.showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list", Method.GET, null, model);
	}

	@GetMapping(value = "/board/list")
	public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
		System.out.println("[1] 컨트롤러를 호출함 /board/list");
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);
		System.out.println("[8] 컨트롤러에서 boardList 리턴");

		return "board/list";
	}

	@GetMapping(value = "/board/view")
	public String openBoardDetail(@RequestParam(value = "idx", required = false) Long idx, Model model) {

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
	public String deleteBoard(@RequestParam(value = "idx", required = false) Long idx, Model model) {

		if (idx == null) {
			return UiUtils.showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list", Method.GET, null, model);
		}

		try {
			boolean isDeleted = boardService.deleteBoard(idx);
			System.out.println("isDeleted : " + isDeleted);
			if (isDeleted == false) {
				System.out.println("삭제 실패");
				return UiUtils.showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return UiUtils.showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list", Method.GET, null,
					model);
		} catch (Exception e) {
			return UiUtils.showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list", Method.GET, null, model);
		}

		return UiUtils.showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list", Method.GET, null, model);
	}

}
