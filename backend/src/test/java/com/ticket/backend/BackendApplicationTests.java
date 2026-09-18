package com.ticket.backend;

import com.ticket.backend.domain.Users;
import com.ticket.backend.dto.auth.LoginRequest;
import com.ticket.backend.dto.auth.LoginResponse;
import com.ticket.backend.mapper.UserMapper;
import com.ticket.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private UserMapper userMapper;

	@Test
	void findByEmailTest() {
		Users users = userMapper.findByEmail("test@test.com");

		if (users == null) {
			System.out.println("사용자를 찾지 못했습니다.");
			return;
		}

		System.out.println("userId = " + users.getUserId());
		System.out.println("userId = " + users.getUserId());
		System.out.println("userId = " + users.getUserId());
		System.out.println("userId = " + users.getUserId());
	}

	//동일 계정 동시 구매 요청 500건 테스트
	@Test
	void orderConcurrencyTest() {

		long ticketId = 6L;

		String accessToken = System.getenv("TEST_ACCESS_TOKEN");

		if (accessToken == null || accessToken.isBlank()) {
			throw new IllegalStateException("Access Token을 설정해주세요.");
		}

		HttpClient client = HttpClient.newHttpClient();

		String url = "http://localhost:8080/api/tickets/" + ticketId + "/orders";

		//티켓 1장 구매 요청
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"quantity\":1}"))
				.build();

		List<CompletableFuture<HttpResponse<String>>> requests = new ArrayList<>();

		//구매 요청 500개 전송
		for (int i = 0; i < 500; i++){
			requests.add(
					client.sendAsync(
							request, HttpResponse.BodyHandlers.ofString()
					)
			);
		}

		//요청 끝날때까지 기다림
		CompletableFuture.allOf(
				requests.toArray(new CompletableFuture[0])
		).join();

		int success = 0;
		int fail = 0;

		//응답 결과
		for (CompletableFuture<HttpResponse<String>> future : requests) {
			HttpResponse<String> response = future.join();

			if (response.statusCode() == 200) {
				success++;
			} else if (response.statusCode() == 400 && response.body().contains("티켓 재고가 부족합니다.")) {
				fail++;
			} else {
				throw new AssertionError(
						"예상하지 못한 응답: "
								+ response.statusCode()
								+ " / "
								+ response.body()
				);
			}
		}
		System.out.println("구매 성공 = " + success);
		System.out.println("재고 부족 실패 = " + fail);

		assertEquals(10, success);
		assertEquals(490, fail);
	}

	@Test
	void ccancelConcurrencyTest() throws Exception{
		long orderId = 4L;

		String accessToken = System.getenv("TEST_ACCESS_TOKEN");
		if (accessToken == null || accessToken.isBlank()) {
			throw new IllegalStateException("Access Token을 설정해주세요.");
		}

		HttpClient client = HttpClient.newHttpClient();

		// 동시 취소 테스트 전에 Access Token 확인
		HttpRequest checkRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/api/orders/me"))
				.header("Authorization", "Bearer " + accessToken)
				.GET()
				.build();

		HttpResponse<String> checkResponse = client.send(
				checkRequest,
				HttpResponse.BodyHandlers.ofString()
		);

		System.out.println("인증 확인 결과 = " + checkResponse.statusCode());

		String url = "http://localhost:8080/api/tickets/" + orderId + "/cancel";

		//주문 취소 요청
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Authorization", "Bearer " + accessToken)
				.method("PATCH", HttpRequest.BodyPublishers.noBody())
				.build();

		List<CompletableFuture<HttpResponse<String>>> requests = new ArrayList<>();

		//취소 요청 500건
		for (int i = 0; i < 500; i++) {
			requests.add(
					client.sendAsync(
							request, HttpResponse.BodyHandlers.ofString()
					)
			);
		}

		CompletableFuture.allOf(
				requests.toArray(new CompletableFuture[0])
		).join();

		int success = 0;
		int fail = 0;

		for (CompletableFuture<HttpResponse<String>> future : requests) {
			HttpResponse<String> response = future.join();

			if (response.statusCode() == 200) {
				success++;
			} else if (response.statusCode() == 400 && response.body().contains("이미 취소된 주문입니다.")) {
				fail++;
			} else {
				throw new AssertionError("예상하지 못한 응답: " + response.statusCode() + "/" + response.body());
			}
		}

		System.out.println("취소 성공 = " + success);
		System.out.println("중복 취소 실패 = " + fail);

		assertEquals(1, success);
		assertEquals(499, fail);
	}
}
