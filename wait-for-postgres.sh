#!/bin/bash

host="$1"
port="$2"
shift 2
cmd="$@"

echo "Waiting for Postgres to be ready at $host:$port..."

until pg_isready -h "$host" -p "$port" -U "postgres"; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done

>&2 echo "Postgres is up - executing command"
exec $cmd
