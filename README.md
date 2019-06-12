# SoftTracker

SoftTracker is a tracking and guiding platform for software engineering training based on agile development, which can unit test and deploy the project uploaded by students automatically.

## Get Start

You can follow the guide below to deploy the back-end SoftTracker in your environment. Please install docker or a virtual machine, such as VirtualBox before you install.

### GitLab CE

I prefer to install GitLab CE on virtual machine rather than docker, cuz it may have conflicts with the port of other applications, like ssh or http. Read [GitLab official docs](https://about.gitlab.com/install/) and install it according to what system on your server. In this case, `192.168.56.101` is the static ip for GitLab CE server.

### GitLab Runner

GitLab Runner, an executor of GitLab CI, is installed on the Docker on my server. There is a [blog](https://angristan.xyz/build-push-docker-images-gitlab-ci/) describing how to install and register on the GitLab. Besides, it introduces how to use GitLab CI to build and push Docker Image automatically.

### SonarQube

Install SonarQube into virtual machine or docker, the docs are [here](https://www.sonarqube.org/downloads/). Be aware that the version of SonarQube MUST be `6.7.x`.

### SoftTracker Back-end

Clone the code from GitHub

```bash
git clone git@github.com:xavier-niu/soft-tracker-backend.git
```

For the security, SoftTracker loads configurations from the **environment variables** which are defined with different ways on different operation system. There are some variables required for the system.

- SOFT_TRACKER_PROFILES_ACTIVE
  The default value is `dev`. This item is for `spring.profiles.active`, more information please view [this page](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html).
- SOFT_TRACKER_DB_HOST: MySQL database host, the default value is `localhost`.
- SOFT_TRACKER_DB_PORT: MySQL database port, the default value is `3306`.
- SOFT_TRACKER_DB_NAME: MySQL database name, **this item is required.**
- SOFT_TRACKER_DB_USERNAME: MySQL database username, **this item is required.**
- SOFT_TRACKER_DB_PASSWORD: MySQL database password, **this item is required.**
- SOFT_TRACKER_REDIS_HOST: Redis host, the default value is `localhost`.
- SOFT_TRACKER_REDIS_PORT: Redis port, the default value is `6379`.
- SOFT_TRACKER_GITLAB_OAUTH_ADMIN_ACCESS_TOKEN: GitLab admin access token, **this item is required.** Please follow the below steps to get it and fill it into environment variables.
  - Go to the settings page for the root(admin) user![](http://res.niuxuewei.com/2019-06-12-090347.png)
  - Click "Access Tokens" on the left bar and create a personal access token for SoftTracker.![image-20190612170733926](http://res.niuxuewei.com/2019-06-12-090734.png)
  - You will get a random string. Please copy it into `SOFT_TRACKER_GITLAB_OAUTH_ADMIN_ACCESS_TOKEN`.
- SOFT_TRACKER_GITLAB_HOST: GitLab host,  **this item is required.** In this case, `192.168.56.101` should be filled. DO NOT add extra elements except the ip or domain of the GitLab deployed by yourself.
- SOFT_TRACKER_UPLOAD_FOLDER: Specify the files user uploaded are copied into which folder, **this item is required.**
- SOFT_TRACKER_SONAR_HOST: SonarQube host, **this item is required.**
- SOFT_TRACKER_SONAR_PORT: SonarQube port, the default value is `9000`.

The code for environment variables is shown below.

```bash
# SOFT_TRACKER
export SOFT_TRACKER_DB_NAME=
export SOFT_TRACKER_DB_USERNAME=
export SOFT_TRACKER_DB_PASSWORD=
export SOFT_TRACKER_GITLAB_OAUTH_ADMIN_ACCESS_TOKEN=
export SOFT_TRACKER_GITLAB_HOST=
export SOFT_TRACKER_UPLOAD_FOLDER=
export SOFT_TRACKER_SONAR_HOST=
```

Install dependencies with Maven and package project.

```bash
mvn install
mvn package
```

Run the project.

```bash
# please replace the xxx.jar to the jar you packaged
java -jar ./target/xxx.jar
```

### SoftTracker Front-end

Install Node.JS from [here](https://nodejs.org/en/).

Install yarn from [here](https://yarnpkg.com/en/).

Clone from GitHub.

```bash
git@github.com:xavier-niu/soft-tracker-fontend.git
```

Install js dependencies.

```bash
yarn install
```

Run the project.

```bash
yarn run dev
```

