package com.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.domain.BoardDTO;

@Mapper
public interface BoardMapper {

	// Create
	public int insertBoard(BoardDTO params);

	// Read
	public BoardDTO selectBoardDetail(Long idx);
	public List<BoardDTO> selectBoardList();

	// Update
	public int updateBoard(BoardDTO params);

	// Delete
	public int deleteBoard(Long idx);

	// count
	public int selectBoardTotalCount();

}
