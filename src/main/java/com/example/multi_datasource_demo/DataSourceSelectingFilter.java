package com.example.multi_datasource_demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DataSourceSelectingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var dataSourceKey = request.getHeader("DataSource");
        DataSourceContextHolder.setDataSource(dataSourceKey);
        filterChain.doFilter(request, response);
        DataSourceContextHolder.clearDataSource();
    }
}
