FROM piwik
RUN apt-get update
RUN apt-get install -y build-essential gfortran libblas-dev r-base python-dev graphviz wget libssl-dev r-cran-ggplot2
RUN R -e 'install.packages(c("glmnet", "optparse", "limSolve", "RUnit", "abind", "RJSONIO"), repos="http://cran.rstudio.com/")'

ADD CleanInsights /var/www/html/plugins/CleanInsights
