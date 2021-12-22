package com.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.board.domain.BoardDTO;
import com.board.service.BoardService;

@SpringBootTest
public class ServiceTests {
	
	@Autowired
	private BoardService boardService;
	
	@Test
	public void testOfInsert() {
		BoardDTO params = new BoardDTO();
		params.setTitle("서비스테스트제목");
		params.setContent("서비스테스트내용");
		params.setWriter("서비스테스트작성자");

		boolean result = boardService.registerBoard(params);
		System.out.println(":: Insert Result - " + result + " ::");
	}
	

}
