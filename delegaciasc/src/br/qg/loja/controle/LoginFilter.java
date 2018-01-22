package br.qg.loja.controle;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */

public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 HttpServletRequest req = (HttpServletRequest) request;
	     HttpServletResponse res = (HttpServletResponse) response;
	     boolean meetConditions=false;  
	     
         String uri = req.getRequestURI();
         //System.out.println(uri);
       
        HttpSession session = req.getSession(false);
        
        if (session!=null){
        LoginAdmin internal = (LoginAdmin) session.getAttribute("loginAdmin");
        Login external = (Login) session.getAttribute("login");
       // FacesContext context = FacesContext.getCurrentInstance();
       // if(context!= null){
	     //   LoginAdmin internal = context.getApplication().evaluateExpressionGet(context, "#{loginAdmin}", LoginAdmin.class);
	       // Login external=context.getApplication().evaluateExpressionGet(context, "#{login}", Login.class);
	      
        
	        if(internal!=null && internal.getAdmin()){
	        	chain.doFilter(request, response);//usuário já está autenticado e é admin. Pode fazer acessar qualquer endereço.
	        	meetConditions=true;
	        	return;
	        }
	        
	        if(external!=null && external.getObreiro()!=null && uri.contains("meus_dados.xhtml")){
	        	chain.doFilter(request, response);
	        	meetConditions=true;
	        	return;
	        }
       }
        
        if(uri.contains("index.xhtml") || uri.contains("login.xhtml")){
        	chain.doFilter(request, response);//usuário deseja se autenticar
        	meetConditions=true;
        	return;
        }
        
        
        if(!meetConditions){
            res.sendRedirect("login.xhtml");//redirect to login scrren
            return;
        	//req.getRequestDispatcher("login.xhtml").forward(request, response);
        }

	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
