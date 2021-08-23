package me.benny.practice.spring.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class StopwatchFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch(request.getServletPath());
        stopWatch.start(); // stop watch 시작
        filterChain.doFilter(request, response);
        stopWatch.stop(); // stop watch 종료
        // Log  StopWatch '/login': running time = 150545041 ns
        log.info(stopWatch.shortSummary());
    }
}
