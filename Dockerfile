FROM archlinux:latest

RUN pacman -Syu --noconfirm \
        git \
        ansible \
        sshpass \
        python3 \
        python-pip

RUN pip install junit_xml

# Disable strict host checking
ENV ANSIBLE_HOST_KEY_CHECKING=False

WORKDIR /ansible

RUN useradd -ms /bin/bash ansible
USER ansible