package com.board.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.domain.BoardDTO;
import com.board.mapper.BoardMapper;
import com.board.paging.Criteria;
import com.board.paging.PaginationInfo;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;
		if (params.getIdx() == null) {
			// idx가 없으면 글을 새로 작성
			queryResult = boardMapper.insertBoard(params);
		} else {
			// idx가 있으면 글을 수정
			queryResult = boardMapper.updateBoard(params);
		}

//		// 트랜잭션 테스트용
//		BoardDTO board = null;
//		System.out.println("***** 여기가 테스트 ********");
//		System.out.println(board.getTitle());

		return (queryResult == 1) ? true : false;
	}

	@Override
	public BoardDTO getBoardDetail(Long idx) {
		return boardMapper.selectBoardDetail(idx);
	}

	@Override
	public boolean deleteBoard(Long idx) {
		int queryResult = 0;
		BoardDTO board = boardMapper.selectBoardDetail(idx);
		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}
		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		System.out.println("[2] 서비스를 호출함 getBoardList");
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);
		System.out.println("[3] 전체 글 개수를 셈 (" + boardTotalCount + "개)");

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);

		params.setPaginationInfo(paginationInfo);

		if (boardTotalCount > 0) {
			System.out.println("[7] 게시글 리스트 검색");
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}

	@Override
	public boolean cntPlus(Long idx) {
		return boardMapper.cntPlus(idx);
	}

}
