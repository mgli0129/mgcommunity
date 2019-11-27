package com.mg.community.mapper;

import com.mg.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,content,tag,comment_count,view_count,like_count," +
            "gmt_create,gmt_modified,creator) values (#{title},#{content},#{tag}," +
            "#{commentCount},#{viewCount}," +
            "#{likeCount},#{gmtCreate},#{gmtModified},#{creator})")
    public void insertQuestion(Question question);

    @Select("select * from question where id = #{id}")
    public Question selectQuestionById(@Param("id") int id);

    @Select("select * from question where title = #{title}")
    public Question findQuestionByTitle(@Param("title") String title);

    @Select("select * from question")
    List<Question> selectAll();

    @Select("select * from question where creator = #{creator}")
    public List<Question> selectQuestionByCreator(@Param("creator") int creator);
}