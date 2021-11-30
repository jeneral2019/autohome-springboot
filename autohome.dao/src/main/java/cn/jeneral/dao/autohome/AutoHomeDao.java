package cn.jeneral.dao.autohome;

import cn.jeneral.dao.common.MyBatisRepository;
import cn.jeneral.entity.CarCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yuxiangfeng
 */
@MyBatisRepository
public interface AutoHomeDao {

    List<CarCategory> selectById(Long id);

    int batchInsert(@Param("carCategoryList") List<CarCategory> carCategoryList);

    List<CarCategory> selectByLevelAndStatus(@Param("level") Integer level, @Param("status") int status);

    int modifyStatus(@Param("id") Long specId, @Param("status") Integer status);
}
