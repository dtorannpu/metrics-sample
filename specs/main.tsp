import "@typespec/http";
import "@typespec/openapi";
import "@typespec/openapi3";

using Http;
using OpenAPI;

/**
 * Title
 */
@service(#{ title: "Title" })
@info(#{ version: "1.0.0", contact: #{ url: "http://localhost" } })
namespace Title;

@summary("Post")
model Post {
    id: integer;
    title: string;
    body: string;
}

model CreatePostRequest {
    @minLength(1)
    @maxLength(100)
    title: string;

    @minLength(1)
    @maxLength(5000)
    body: string;
}

model UpdatePostRequest {
    @minLength(1)
    @maxLength(100)
    title: string;

    @minLength(1)
    @maxLength(5000)
    body: string;
}

/**
 * 投稿一覧取得
 */
@tag("post")
@route("/posts")
@get
@summary("投稿一覧取得")
op `get-post`(): Body<Post[]>;

/**
 * 投稿作成
 */
@tag("post")
@route("/posts")
@post
@summary("投稿作成")
op `create-post`(@body body: CreatePostRequest): OkResponse;

/**
 * Post削除
 */
@tag("post")
@route("/posts/{id}")
@delete
@summary("投稿削除")
op `delete-post`(@path id: integer): OkResponse;

/**
 * 投稿をIDで取得
 */
@tag("post")
@route("/posts/{id}")
@get
@summary("投稿をIDで取得")
op `get-post-id`(@path id: integer): Post | NotFoundResponse;

/**
 * 投稿更新
 */
@tag("post")
@route("/posts/{id}")
@patch(#{ implicitOptionality: false })
@summary("投稿更新")
op `update-post`(@path id: integer, @body body: UpdatePostRequest): OkResponse;
