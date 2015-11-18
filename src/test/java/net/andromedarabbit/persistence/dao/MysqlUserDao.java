package net.andromedarabbit.persistence.dao;

/**
 * Created by tywin on 2015. 11. 16..
 */
import net.andromedarabbit.persistence.mapper.MysqlUserMapper;
import net.andromedarabbit.persistence.model.MysqlUser;
import net.andromedarabbit.persistence.model.MysqlUserExample;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class MysqlUserDao extends SqlSessionDaoSupport {

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public List<MysqlUser> getUsers(MysqlUserExample example, RowBounds bounds) {
        MysqlUserMapper mapper = getSqlSession().getMapper(MysqlUserMapper.class);
        return mapper.selectByExampleWithRowbounds(example, bounds);
    }
}
