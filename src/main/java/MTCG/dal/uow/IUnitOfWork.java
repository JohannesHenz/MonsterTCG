package MTCG.dal.uow;

public interface IUnitOfWork {

    void beginTransaction();
    void commit();
    void rollback();
}
