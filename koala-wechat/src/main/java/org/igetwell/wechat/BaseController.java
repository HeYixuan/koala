package org.igetwell.wechat;

import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BaseController {

    protected static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();

    protected static ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();

    protected static ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();

    @ModelAttribute
    private void init(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        this.session.set(session);
        this.request.set(request);
        this.response.set(response);
    }


    public void renderXml(String renderText){
        try {
            response.get().setCharacterEncoding("UTF-8");
            response.get().setContentType("text/xml; charset=UTF8");
            response.get().getWriter().write(renderText);
            response.get().getWriter().flush();
            response.get().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderHtml(String renderText){
        try {
            response.get().setCharacterEncoding("UTF-8");
            response.get().setContentType("text/html;charset=UTF-8");
            response.get().getWriter().write(renderText);
            response.get().getWriter().flush();
            response.get().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderJson(String renderText){
        try {
            response.get().setCharacterEncoding("UTF-8");
            response.get().setContentType("application/json;charset=UTF-8");
            response.get().getWriter().write(renderText);
            response.get().getWriter().flush();
            response.get().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void render(String renderText){
        try {
            response.get().setCharacterEncoding("UTF-8");
            response.get().getWriter().write(renderText);
            response.get().getWriter().flush();
            response.get().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
