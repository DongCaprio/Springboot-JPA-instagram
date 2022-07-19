package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{
 //얘는 클래스 아니라 interface다
	
	@Modifying //INSERT, DELETEE, UPDATEE를 네이티브 쿼리로 작성하려면 해당 어노테이션을 붙여주서야한다.
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId); //@Quary의 " "안에 :뒤에 단어와 일치하는 파라미터가 있다면 쿼리에 들어가게 된다.
	// 만약 return이 int이면?
	// (원래부터정해진룰)위 메소드는 성공하면 1리턴, 실패시 -1리턴
	// 1, -1의 기준은 변경된 행의 개수만 리턴됨 (10개 변경되었으면 10 나와야된다)
	//0 은 insert 1개도 안됌, -1은 에러!!!
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId =:toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);
	// 위 메소드는 성공하면 1리턴, 실패시 -1리턴(return타입이 int일때)
	
	//select만 하는건 @Modifying필요없다
	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(int principalId, int pageUserId);

	@Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(int pageUserId);
}
