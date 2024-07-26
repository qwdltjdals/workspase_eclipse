package filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;


@WebFilter(filterName = "encodingFilter")// url에서 어떠한 경로로 거치건 간에 필터를 거쳐라 원래 경로에 filter가 중간에 낌 -> 기존 tomcat - servlet / 변경후 tomcat - filter - server
public class EncodingFilter extends HttpFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//전처리
		System.out.println("전처리");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response); // 다음필터 또는 서블릿 호출
		//후처리
		System.out.println("후처리");
	}
}
