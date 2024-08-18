##!/bin/bash

# to setup ssh, you need the following:
        # Remove old certbot using the package manager used for installing it.
        # Install snap if already not installed.
        # domain or subdomain with records A(IP4), AAAA(IP6) and CNAME(www.) set.
        # Then we can start installing free ssl certificate.
        # Port 80 and 443 have to be free.
# Installing Certbot and ensure that the certbot command can be run:
sudo apt-get remove certbot # Please edit, if you have used different package manager for installing certbot.
sudo apt update
sudo apt install snapd
sudo snap install core; sudo snap refresh core
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot
# Running Certbot
sudo ufw allow 80
sudo ufw allow 443
sudo certbot certonly --standalone -d server.grykely.de
# If everything fine, the following handles the renewals of the certificate and updating needed service.
sudo echo -e "\n# custom hooks" >> /etc/letsencrypt/renewal/server.grykely.de.conf
sudo echo "pre_hook = /home/ziad/workspace/ssl-scripts/preparation-ssl.sh" >> /etc/letsencrypt/renewal/server.grykely.de.conf
sudo echo "post_hook = /home/ziad/workspace/ssl-scripts/update-ssl.sh" >> /etc/letsencrypt/renewal/server.grykely.de.conf
