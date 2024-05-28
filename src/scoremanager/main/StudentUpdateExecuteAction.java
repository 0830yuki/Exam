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


public class StudentUpdateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ローカル変数の宣言１
		StudentDao studentDao = new StudentDao(); // 学生Dao
		boolean isAttend = false; // 在籍フラグ
		HttpSession session = request.getSession(); // セッション
		Teacher teacher =(Teacher)session.getAttribute("user"); // ログインユーザーを取得
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Daoを初期化
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// リクエストパラメーターの取得２
		String entYearString= request.getParameter("ent_year");
		String no = request.getParameter("no");
		String name = request.getParameter("name");
		String classNum = request.getParameter("class_num");
		String isAttendString = request.getParameter("is_attemd");
		// 在籍フラグにチェックが入っていた場合
		if (isAttendString != null) {
			// 在籍フラグを立てる
			isAttend = true;
		}

		// DBからデータ取得３
		Student student = studentDao.get(no); // 学生番号から学生インスタンスを取得
		List<String> list = classNumDao.filter(teacher.getSchool()); // ログインユーザーの学校コードをもとにクラス番号の一覧を取得

		// ビジネスロジック 4
		// DBへデータ保存 5
		// 条件で4～5が分岐
		if (student != null) {
			// 学生が存在していた場合
			// インスタンスに値をセット
			student.setName(name);
			student.setClassNum(classNum);
			student.setAttend(isAttend);
			// 学生を保存
			studentDao.save(student);
		} else {
			errors.put("no", "学生が存在していません");
		}

		// エラーがあったかどうかで手順6～7の内容が分岐
		// レスポンス値をセット 6
		// JSPへフォワード 7
		request.setAttribute("class_num_set", list);

		if(!errors.isEmpty()) { // エラーがあった場合、更新画面へ戻る
			// リクエスト属性をセット
			request.setAttribute("errors", errors);
			request.setAttribute("ent_year", entYearString);
			request.setAttribute("no", no);
			request.setAttribute("name", name);
			request.setAttribute("class_num", classNum);
			request.setAttribute("is_attend", isAttendString);
			request.getRequestDispatcher("student_update.jsp").forward(request, response);
			return;
		}

		request.getRequestDispatcher("student_update_done.jsp").forward(request, response);
	}

}
