package scoremanager.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ローカル変数の宣言
		StudentDao studentDao = new StudentDao(); // 学生Dao
		HttpSession session = request.getSession(); // セッション
		Teacher teacher = (Teacher)session.getAttribute("user"); // ログインユーザーを取得
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Daoを初期化
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// リクエストパラメーターの取得２
		String no = request.getParameter("no"); // 学生番号

		// DBからデータ取得３
		Student student = studentDao.get(no); // 学生番号から学生インスタンスを取得
		List<String> list = classNumDao.filter(teacher.getSchool()); // ログインユーザーの学校コードをもとにクラス番号の一覧を取得

		// ビジネスロジック
		// DBへデータ保存
		// 条件で手順4～6の内容が分岐
		request.setAttribute("class_num_set", list);
		if (student != null) { // 学生が存在していた場合
			request.setAttribute("ent_year", student.getEntYear());
			request.setAttribute("no", student.getNo());
			request.setAttribute("name", student.getName());
			request.setAttribute("class_num", student.getClassNum());
			request.setAttribute("is_attend", student.isAttend());
		} else { // 学生が存在していなかった場合
			errors.put("no", "学生が存在していません");
			request.setAttribute("errors", errors);
		}
		// JSPへフォワード７
		request.getRequestDispatcher("student_update.jsp").forward(request, response);
	}

}
