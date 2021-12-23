package com.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import com.board.domain.BoardDTO;
import com.board.mapper.BoardMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
public class MapperTests {

	@Autowired
	private BoardMapper boardMapper;

	@Test
	public void testOfInsert() {
		BoardDTO params = new BoardDTO();
		params.setTitle("title");
		params.setContent("content");
		params.setWriter("writer");

		int result = boardMapper.insertBoard(params);
		System.out.println(":: Insert Result - " + result + " ::");
	}

	@Test
	public void testOfSelectDetail() {
		BoardDTO board = boardMapper.selectBoardDetail((long) 2);
		try {
			String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);
			System.out.println(":: SelectDetail Result - " + boardJson + " ::");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOfUpdate() {
		BoardDTO params = new BoardDTO();
		params.setTitle("제목");
		params.setContent("내용");
		params.setWriter("쓰니");
		params.setIdx((long) 2);

		int result = boardMapper.updateBoard(params);
		System.out.println(":: Update Result - " + result + " ::");

		if (result > 0) {
			BoardDTO board = boardMapper.selectBoardDetail((long) 2);
			try {
				String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);
				System.out.println(":: SelectDetail Result - " + boardJson + " ::");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testOfDelete() {
		int result = boardMapper.deleteBoard((long) 1);

		if (result > 0) {
			BoardDTO board = boardMapper.selectBoardDetail((long) 1);
			try {
				String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);
				System.out.println(":: Delete Result - " + boardJson + " ::");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testSelectList() {
		int boardTotalCount = boardMapper.selectBoardTotalCount();
		System.out.println(":: boardTotalCount - " + boardTotalCount + " ::");

		if (boardTotalCount > 0) {
			List<BoardDTO> boardList = boardMapper.selectBoardList();
			if (CollectionUtils.isEmpty(boardList) == false) {
				for (BoardDTO board : boardList) {
					System.out.println(":: 글 정보 - " + board.getTitle() + " by " + board.getWriter() + " ::");
				}
			}
		}

	}

	@Test
	public void testOfMultiInsert() {
		BoardDTO params = new BoardDTO();
		int count = 0;
		for (int i = 5; i < 20; i++) {
			params.setTitle(i + "번글 제목");
			params.setContent(i + "번글 내용");
			params.setWriter(i + "번글 쓰니");
			boardMapper.insertBoard(params);
			count++;
		}
		System.out.println(":: 총 작성글 수 - " + count + "개 ::");
	}
	
	@Test
	public void testOfCntPlus( ) {
		Long idx = (long) 5;
		boolean result = boardMapper.cntPlus(idx);
		System.out.println(":: 결과 - " + result);
	}

}
