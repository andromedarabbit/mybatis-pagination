package net.andromedarabbit.persistence.mapper;

import java.util.List;
import net.andromedarabbit.persistence.model.MysqlUser;
import net.andromedarabbit.persistence.model.MysqlUserExample;
import net.andromedarabbit.persistence.model.MysqlUserKey;
import net.andromedarabbit.persistence.model.MysqlUserWithBLOBs;
import org.apache.ibatis.session.RowBounds;

public interface MysqlUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    int countByExample(MysqlUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    List<MysqlUserWithBLOBs> selectByExampleWithBLOBsWithRowbounds(MysqlUserExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    List<MysqlUserWithBLOBs> selectByExampleWithBLOBs(MysqlUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    List<MysqlUser> selectByExampleWithRowbounds(MysqlUserExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    List<MysqlUser> selectByExample(MysqlUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbggenerated
     */
    MysqlUserWithBLOBs selectByPrimaryKey(MysqlUserKey key);
}