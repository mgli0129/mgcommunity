package com.mg.community.mapper;

import com.mg.community.model.Question;
import com.mg.community.model.QuestionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    long countByExample(QuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    int deleteByExample(QuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    int insert(Question record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    int insertSelective(Question record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    List<Question> selectByExampleWithBLOBs(QuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    List<Question> selectByExample(QuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    int updateByExampleSelective(@Param("record") Question record, @Param("example") QuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    int updateByExampleWithBLOBs(@Param("record") Question record, @Param("example") QuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Sat Feb 08 12:31:53 CST 2020
     */
    int updateByExample(@Param("record") Question record, @Param("example") QuestionExample example);
}