package servlet.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDao;
import entity.User;

@WebServlet("/api/mypage/password/edit")
public class PasswordEditServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("authentication");
		String password = req.getParameter("password");
		String checkPassword = req.getParameter("checkPassword");
		
		if(!password.equals(checkPassword)) { // 비밀번호 확인
			responsePasswordEditError(resp, "비밀번호가 일치하지 않습니다.\\n다시 입력하세요.");
			return;
		}
		
		if(!BCrypt.checkpw(password, user.getPassword())) { // 비밀번호 확인
			responsePasswordEditError(resp, "기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.\\n다시 입력하세요.");
			return;
		}
		
		User modifUser = User.builder()
				.userId(user.getUserId())
				.password(BCrypt.hashpw(password, BCrypt.gensalt()))
				.build();
		UserDao.updatePassword(modifUser);
		user.setPassword(modifUser.getPassword());
		
		StringBuilder responseBody = new StringBuilder();
		responseBody.append("<script>");
		responseBody.append("alert('수정 완료.\\n다시 로그인 하세요.');");
		responseBody.append("location.href='/ssa/logout';");
		responseBody.append("</script>");
		resp.setContentType("text/html");
		resp.getWriter().println(responseBody.toString());
		
	}
	
	private void responsePasswordEditError(HttpServletResponse resp, String msg) throws IOException {
		StringBuilder responsebody = new StringBuilder();
		responsebody.append("<script>");
		responsebody.append("alert('");
		responsebody.append(msg);
		responsebody.append("');");
		responsebody.append("history.back();");
		responsebody.append("</script>");
		resp.setContentType("text/html");
		resp.getWriter().println(responsebody.toString());
	}
}
