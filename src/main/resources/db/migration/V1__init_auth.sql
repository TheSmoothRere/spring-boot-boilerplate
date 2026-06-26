-- ============================================================================
-- Common Functions
-- ============================================================================

create or replace function auth.handle_updated_at()
    returns trigger
    language plpgsql
as
$$
begin
    new.updated_at = now();
    return new;
end;
$$;

-- ============================================================================
-- Enums
-- ============================================================================

create type auth.user_status as enum ('ACTIVE', 'INACTIVE', 'LOCKED');

-- ============================================================================
-- Roles
-- ============================================================================

create table auth.roles
(
    id          uuid primary key     default pg_catalog.gen_random_uuid(),
    name        varchar(60) not null unique,
    description text        not null,

    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now()
);

create trigger trg_roles_updated_at
    before update
    on auth.roles
    for each row
    when (old is distinct from new)
execute function auth.handle_updated_at();

-- feed default roles
insert into auth.roles (name, description)
values ('ADMIN', 'System administrator with full access'),
       ('USER', 'Standard application user');

-- ============================================================================
-- Users
-- ============================================================================

create table auth.users
(
    id         uuid primary key          default pg_catalog.gen_random_uuid(),
    username   varchar(100)     not null unique,
    password   text             not null,
    status     auth.user_status not null default 'ACTIVE',

    created_at timestamptz      not null default now(),
    updated_at timestamptz      not null default now()
);

create trigger trg_users_updated_at
    before update
    on auth.users
    for each row
    when (old is distinct from new)
execute function auth.handle_updated_at();

-- ============================================================================
-- User Roles
-- ============================================================================

create table auth.user_roles
(
    user_id    uuid        not null,
    role_id    uuid        not null,

    created_at timestamptz not null default now(),

    constraint pk_user_roles primary key (user_id, role_id),

    constraint fk_user_roles_user foreign key (user_id) references auth.users (id) on delete cascade,

    constraint fk_user_roles_role foreign key (role_id) references auth.roles (id) on delete cascade
);