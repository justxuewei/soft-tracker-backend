package com.niuxuewei.lucius.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.niuxuewei.lucius.core.enumeration.GitLabHttpRequestAuthMode;
import com.niuxuewei.lucius.core.request.GitlabHttpRequest;
import com.niuxuewei.lucius.entity.dto.GitLabCommitDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProjectServiceImplTests {

    @Resource
    private GitlabHttpRequest gitlabHttpRequest;

    @Resource
    private IProjectService projectService;

    @Test
    public void testCalculateContributionScore() throws ParseException {
        // 时间
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startAt = dateFormat.parse("2019-05-20");
        Date endAt = new Date();

        // 获取全部项目
        String projectsJson = gitlabHttpRequest.get(GitLabHttpRequestAuthMode.ADMIN_AUTH, "/projects");
        log.debug(projectsJson);
        List<GitLabProject> projects = JSONArray.parseArray(projectsJson, GitLabProject.class);

        List<ContributionStatistics> contributionStatisticsList = new ArrayList<>();

        for (GitLabProject project: projects) {
            String commitsJson= gitlabHttpRequest.get(GitLabHttpRequestAuthMode.ADMIN_AUTH,
                    String.format("/projects/%d/repository/commits", project.getId()));
            List<GitLabCommitDTO> commits = JSON.parseArray(commitsJson, GitLabCommitDTO.class);

            double contributionScore = projectService.calculateContributionScore(100.0, project.getName(),
                    startAt, endAt, commits, true);

            ContributionStatistics statistics = new ContributionStatistics();
            statistics.setId(project.getId());
            statistics.setName(project.getName());
            statistics.setContributionScore(contributionScore);

            contributionStatisticsList.add(statistics);
        }

        log.debug("---------------- RESULT -----------------");
        for (ContributionStatistics statistics: contributionStatisticsList) {
            log.debug("project name: {}, contribution score: {}", statistics.getName(), statistics.getContributionScore());
        }
        log.debug("-----------------------------------------");


    }

}

@Data
class GitLabProject {

    private Integer id;

    private String name;

}

@EqualsAndHashCode(callSuper = true)
@Data
class ContributionStatistics extends GitLabProject {

    private double contributionScore;

}
