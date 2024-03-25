package com.example.multi_datasource_demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class DataSourceSelectingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var dataSource = request.getHeader("DataSource");
        if (dataSource != null) {
            DataSourceContextHolder.setDataSource(dataSource);
        }
        filterChain.doFilter(request, response);
        DataSourceContextHolder.clearDataSource();
    }
}
