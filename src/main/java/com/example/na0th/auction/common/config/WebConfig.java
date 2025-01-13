package com.example.na0th.auction.common.config;

import com.example.na0th.auction.common.resolver.SearchConditionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SearchConditionResolver searchConditionResolver;

    public WebConfig(SearchConditionResolver searchConditionResolver) {
        this.searchConditionResolver = searchConditionResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(searchConditionResolver);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/bidhtml/{auctionId}")
                .setViewName("forward:/bidActive.html");
    }
}
