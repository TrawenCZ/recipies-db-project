# Aplikace pro správu a užití databáze receptů

***

## Požadavky
- [ ] na konci semestru mít **kompletní single-user aplikaci** (připravenou k užití, bez účtů)
- [ ] ukládání, mazání, aktualizace, import, export **receptů**
- [ ] ukládání, mazání, aktualizace, import, export **ingrediencí**
- [ ] ukládání, mazání, aktualizace, import, export **vlastních jednotek**
- [ ] **základní jednotky** zavedeny defaultně (litr, ml, gram, kus, ...)
- [ ] možnost **kategorizace** receptů (každý recept maximálně **jednu** kategorii)
- [ ] **vyhledávání** s **filtrováním** (možnost dle více podmínek):
    podle nutriční hodnoty
    podle ingrediencí
    podle kategorií
    podle času na přípravu = od-do
    podle počtu porcí = od-do
- [ ] tlačítko **reset** pro filtrování a vyhledávání

#### Recepty
```
- název (unikátní)
- kategorie (max. 1)
- popis (krátký, např. "Tohle je můj koláč, který mi doporučila bábinka")
- potřebný čas
- počet porcí
- ingredience
- instrukce k přípravě
```

#### Ingredience
```
- název (unikátní)
- energetická hodnota  (stačí jedna jednotka, ale musí existovat možnost vzít hodnotu z obalu,
                        kde bývá např. na 90g a automaticky převést na 1g)
```

#### Základní jednotky
```
- název (unikátní)
- hodnota (je-li převoditelná do základu)
```

#### Uživatelské jednotky
```
- název (unikátní i s jednotkami)
- hodnota v nějaké základní jednotce
```

***

## Milníky
- [ ] 03.10. = **GUI design** (plně klikatelný prototyp bez navazane logiky)
- [ ] 24.10. = **Business logic, import, export** implemented
- [ ] 14.11. = **DB Operations** implemented
- [ ] 05.12. = **Non-blocking GUI**, completion of the project

***

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.fi.muni.cz/xskacel/pv168-recipies-db.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.fi.muni.cz/xskacel/pv168-recipies-db/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

## Autoři
Team lead - **Marek Skácelík**
Developer - **Jan Martinek**
Developer - **Adam Slíva**
Developer - **Radim Stejskal**