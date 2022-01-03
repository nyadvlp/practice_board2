package com.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.domain.BoardDTO;
import com.board.paging.Criteria;

@Mapper
public interface BoardMapper {

	// Create
	public int insertBoard(BoardDTO params);

	// Read
	public BoardDTO selectBoardDetail(Long idx);
	public List<BoardDTO> selectBoardList(BoardDTO params);

	// Update
	public int updateBoard(BoardDTO params);

	// Delete
	public int deleteBoard(Long idx);

	// count
	public int selectBoardTotalCount(BoardDTO params);

	public boolean cntPlus(Long idx);

}
