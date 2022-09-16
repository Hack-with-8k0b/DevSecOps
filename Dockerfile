# Working Docker file to host a sample static web page
FROM centos:latest
MAINTAINER nbharathkumara@gmail.com
RUN cd /etc/yum.repos.d/
RUN sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*
RUN sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*

RUN yum install -y httpd \
  zip \
 unzip
ADD https://www.free-css.com/assets/files/free-css-templates/download/page283/milina.zip /var/www/html/
WORKDIR /var/www/html
RUN unzip milina.zip
RUN cp -rvf milina-html/* .
RUN rm -rf milina-html milina.zip
CMD ["/usr/sbin/httpd", "-D",  "FOREGROUND"]
EXPOSE 80
