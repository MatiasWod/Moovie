import {
    ActorDto, CommentDto, CommentReportDto,
    DirectorDto,
    GenreDto,
    MediaDto,
    MediaIdListIdDto,
    MoovieListDto, MoovieListReportDto, MoovieListReviewDto, MoovieListReviewReportDto,
    ProviderDto, ReviewDto, ReviewReportDto,
    TvCreatorsDto,
    UserDto
} from "./MockModels";
import MoovieListTypes from "../../api/values/MoovieListTypes";

export const actorDto = new ActorDto(
    73457,
    "Chris Pratt",
    "https://image.tmdb.org/t/p/original/9vHfySKm8fxaCOACAybNt69neTO.jpg",
    process.env.REACT_APP_API_URL + "/actors/73457",
    process.env.REACT_APP_API_URL + "/medias?actorId=73457"
);

export const actorDto2 = new ActorDto(
    1,
    "Carlos Villagrán",
    "https://image.tmdb.org/t/p/original/9vHfySKm8fxaCOACAybNt69neTO.jpg",
    process.env.REACT_APP_API_URL + "/actors/1",
    process.env.REACT_APP_API_URL + "/medias?actorId=1"
);

export const directorDto = new DirectorDto({
    directorId: 525,
    name: "Christopher Nolan",
    totalMedia: 1,
    url: process.env.REACT_APP_API_URL + "/directors/525",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?directorId=525"}
)

export const directorDto2 = new DirectorDto({
    directorId: 1,
    name: "Carlo Zemeckis",
    totalMedia: 1,
    url: process.env.REACT_APP_API_URL + "/directors/1",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?directorId=1"}
)

export const tvCreatorsDto = new TvCreatorsDto({
    id: 99898,
    creatorName: "David Chase",
    url: process.env.REACT_APP_API_URL + "/tvCreators/99898",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?tvCreatorId=99898"
});

export const tvCreatorsDto2 = new TvCreatorsDto({
    id: 1,
    creatorName: "David Crane",
    url: process.env.REACT_APP_API_URL + "/tvCreators/1",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?tvCreatorId=1"
});

export const genreDto = new GenreDto({
    genreId: 1,
    genreName: "Action",
    url: process.env.REACT_APP_API_URL + "/genres/1",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?genres=1"
})

export const genreDto2 = new GenreDto({
    genreId: 8,
    genreName: "Drama",
    url: process.env.REACT_APP_API_URL + "/genres/8",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?genres=8",
})

export const providerDto = new ProviderDto({
    providerId: 213,
    providerName: "Netflix",
    logoPath: "https://image.tmdb.org/t/p/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.png",
    url: process.env.REACT_APP_API_URL + "/providers/213",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?providers=213"
})

export const providerDto2 = new ProviderDto({
    providerId: 1,
    providerName: "Apple TV",
    logoPath: "https://image.tmdb.org/t/p/original/wwemzKWzjKYJFfCeiB57q3r4Bcm.png",
    url: process.env.REACT_APP_API_URL + "/providers/1",
    mediasUrl: process.env.REACT_APP_API_URL + "/medias?providers=1"
})

export const mediaDto = new MediaDto({
    type: "Serie",
    actorsUrl: process.env.REACT_APP_API_URL + "/actors?mediaId=190",
    adult: false,
    backdropPath: "https://image.tmdb.org/t/p/original/aRKQdF6AGbhnF9IAyJbte5epH5R.jpg",
    creatorsUrl: process.env.REACT_APP_API_URL + "/tvCreators?mediaId=190",
    genresUrl: process.env.REACT_APP_API_URL + "/genres?mediaId=190",
    id: 190,
    lastAirDate: "2023-08-31T00:00:00-03:00",
    name: "ONE PIECE",
    numberOfEpisodes: 8,
    numberOfSeasons: 1,
    originalLanguage: "en",
    overview: "With his straw hat and ragtag crew, young pirate Monkey D. Luffy goes on an epic voyage for treasure in this live-action adaptation of the popular manga.",
    posterPath: "https://image.tmdb.org/t/p/original/rVX05xRKS5JhEYQFObCi4lAnZT4.jpg",
    providersUrl: process.env.REACT_APP_API_URL + "/providers?mediaId=190",
    releaseDate: "2023-08-31T00:00:00-03:00",
    reviewsUrl: process.env.REACT_APP_API_URL + "/reviews?mediaId=190",
    status: "Returning Series",
    tmdbRating: 4.15,
    totalRating: 0,
    trailerLink: "https://www.youtube.com/watch?v=l6kp780S-os",
    url: process.env.REACT_APP_API_URL + "/medias/190",
    voteCount: 0,
})

export const mediaDto2 = new MediaDto({
    type: "Serie",
    actorsUrl: process.env.REACT_APP_API_URL + "/actors?mediaId=1",
    adult: false,
    backdropPath: "https://image.tmdb.org/t/p/original/aRKQdF6AGbhnF9IAyJbte5epH5R.jpg",
    creatorsUrl: process.env.REACT_APP_API_URL + "/tvCreators?mediaId=1",
    genresUrl: process.env.REACT_APP_API_URL + "/genres?mediaId=1",
    id: 1,
    lastAirDate: "2023-08-31T00:00:00-03:00",
    name: "Severance",
    numberOfEpisodes: 8,
    numberOfSeasons: 1,
    originalLanguage: "en",
    overview: "Mark leads a team of office workers whose memories have been surgically divided between their work and personal lives. But when a mysterious colleague appears outside of work, it begins to unravel the mystery of their jobs.",
    posterPath: "https://image.tmdb.org/t/p/original/rVX05xRKS5JhEYQFObCi4lAnZT4.jpg",
    providersUrl: process.env.REACT_APP_API_URL + "/providers?mediaId=1",
    releaseDate: "2023-08-31T00:00:00-03:00",
    reviewsUrl: process.env.REACT_APP_API_URL + "/reviews?mediaId=1",
    status: "Returning Series",
    tmdbRating: 4.15,
    totalRating: 0,
    trailerLink: "https://www.youtube.com/watch?v=l6kp780S-os",
    url: process.env.REACT_APP_API_URL + "/medias/1",
    voteCount: 0,
})

export const mediaDto3 = new MediaDto({
    type: "Movie",
    actorsUrl: process.env.REACT_APP_API_URL + "/actors?mediaId=2",
    adult: false,
    backdropPath: "https://image.tmdb.org/t/p/original/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg",
    genresUrl: process.env.REACT_APP_API_URL + "/genres?mediaId=2",
    id: 2,
    name: "Oppenheimer",
    originalLanguage: "en",
    overview: "The story of J. Robert Oppenheimer’s role in the development of the atomic bomb during World War II.",
    posterPath: "https://image.tmdb.org/t/p/original/rVX05xRKS5JhEYQFObCi4lAnZT4.jpg",
    providersUrl: process.env.REACT_APP_API_URL + "/providers?mediaId=2",
    releaseDate: "2023-08-31T00:00:00-03:00",
    reviewsUrl: process.env.REACT_APP_API_URL + "/reviews?mediaId=2",
    status: "Released",
    tmdbRating: 4.15,
    totalRating: 0,
    trailerLink: "https://www.youtube.com/watch?v=l6kp780S-os",
    url: process.env.REACT_APP_API_URL + "/medias/2",
    voteCount: 0,
    budget: 100000000,
    directorUrl: process.env.REACT_APP_API_URL + "/directors/525",
    revenue: 890988955,
    runtime: 181
})

export const mediaIdListIdDto = new MediaIdListIdDto({
    mediaId: 190,
    listId: 1,
    customOrder: 1,
    url: process.env.REACT_APP_API_URL + "/lists/1/content/190"
})

export const moovieListDto = new MoovieListDto({
    id: 1,
    name: "My Favorite Movies",
    createdBy: "john_doe",
    description: "A list of my favorite movies.",
    type: 1,
    likes: 1,
    followers: 1,
    mediaCount: 1,
    movieCount: 1,
    images: null,
    url: process.env.REACT_APP_API_URL + "/lists/1",
    abuseReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=1&reportType=1",
    contentUrl: process.env.REACT_APP_API_URL + "/lists/1/content",
    creatorUrl: process.env.REACT_APP_API_URL + "/users/john_doe",
    likesUrl: process.env.REACT_APP_API_URL + "/lists/1/likes",
    followersUrl: process.env.REACT_APP_API_URL + "/lists/1/followers",
    hateReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=1&reportType=0",
    privacyReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=1&reportType=2",
    recommendedListsUrl: process.env.REACT_APP_API_URL + "/lists/?getRecommendedOfListId=1",
    reportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=1",
    reviewsUrl: process.env.REACT_APP_API_URL + "/moovieListReviews?listId=1",
    spamReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=1&reportType=3",
})

export const moovieListReviewDto = new MoovieListReviewDto({
    abuseReportsUrl: process.env.REACT_APP_API_URL + "/moovieListReviewReports?moovieListReviewId=6&reportType=1",
    hateReportsUrl: process.env.REACT_APP_API_URL + "/moovieListReviewReports?moovieListReviewId=6&reportType=0",
    id: 6,
    likesUrl: process.env.REACT_APP_API_URL + "/moovieListReviews/6/likes",
    moovieListUrl: process.env.REACT_APP_API_URL + "/lists/1",
    moovieListid: 1,
    privacyReportsUrl: process.env.REACT_APP_API_URL + "/moovieListReviewReports?moovieListReviewId=6&reportType=2",
    reportsUrl: process.env.REACT_APP_API_URL + "/moovieListReviewReports?moovieListReviewId=6",
    reviewContent: "que tal?",
    reviewLikes: 1,
    spamReportsUrl: process.env.REACT_APP_API_URL + "/moovieListReviewReports?moovieListReviewId=6&reportType=3",
    url: process.env.REACT_APP_API_URL + "/moovieListReviews/6",
    userUrl: process.env.REACT_APP_API_URL + "/users/Adolfo",
    username: "Adolfo",
})

export const watchedMoovieListDto = new MoovieListDto({
    id: 2,
    name: "My Favorite Movies",
    createdBy: "testUser",
    description: "A list of my favorite movies.",
    type: MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.type,
    likes: 1,
    followers: 1,
    mediaCount: 1,
    movieCount: 1,
    images: null,
    url: process.env.REACT_APP_API_URL + "/lists/2",
    abuseReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=2&reportType=1",
    contentUrl: process.env.REACT_APP_API_URL + "/lists/2/content",
    creatorUrl: process.env.REACT_APP_API_URL + "/users/testUser",
    likesUrl: process.env.REACT_APP_API_URL + "/lists/2/likes",
    followersUrl: process.env.REACT_APP_API_URL + "/lists/2/followers",
    hateReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=2&reportType=0",
    privacyReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=2&reportType=2",
    recommendedListsUrl: process.env.REACT_APP_API_URL + "/lists/?getRecommendedOfListId=2",
    reportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=2",
    reviewsUrl: process.env.REACT_APP_API_URL + "/moovieListReviews?listId=2",
    spamReportsUrl: process.env.REACT_APP_API_URL + "/listReports?moovieListId=2&reportType=3",
})

export const commentReportDto = new CommentReportDto({
        commentUrl: process.env.REACT_APP_API_URL + "/comments/16",
        reportId: 1,
        report_date: "2025-02-19T20:08:24.777",
        reportedByUrl: process.env.REACT_APP_API_URL + "/users/Adolfo",
        type: 3,
        url: process.env.REACT_APP_API_URL + "/commentsReports/1"
    }
)

export const reviewReportDto = new ReviewReportDto({
        reviewUrl: process.env.REACT_APP_API_URL + "/reviews/1",
        reportId: 2,
        report_date: "2025-02-19T20:08:24.777",
        reportedByUrl: process.env.REACT_APP_API_URL + "/users/Adolfo",
        type: 3,
        url: process.env.REACT_APP_API_URL + "/reviewReports/2"
    }
)

export const moovieListReportDto = new MoovieListReportDto({
        moovieListUrl: process.env.REACT_APP_API_URL + "/lists/1",
        reportId: 3,
        report_date: "2025-02-19T20:08:24.777",
        reportedByUrl: process.env.REACT_APP_API_URL + "/users/Adolfo",
        type: 3,
        url: process.env.REACT_APP_API_URL + "/listReports/3"
    }
)

export const moovieListReviewReportDto = new MoovieListReviewReportDto({
        moovieListReviewUrl: process.env.REACT_APP_API_URL + "/moovieListReview/1",
        reportId: 4,
        report_date: "2025-02-19T20:08:24.777",
        reportedByUrl: process.env.REACT_APP_API_URL + "/users/Adolfo",
        type: 3,
        url: process.env.REACT_APP_API_URL + "/moovieListReviewReports/4"
    }
)

export const commentDto = new CommentDto({
    abuseReportsUrl: process.env.REACT_APP_API_URL + "/commentsReports?commentId=16&reportType=1",
    commentDislikes: 0,
    commentLikes: 0,
    content: "Hello",
    feedbackUrl: process.env.REACT_APP_API_URL + "/comments/16/feedback",
    hateReportsUrl: process.env.REACT_APP_API_URL + "/commentsReports?commentId=16&reportType=0",
    id: 16,
    mediaId: 120,
    privacyReportsUrl: process.env.REACT_APP_API_URL + "/commentsReports?commentId=16&reportType=2",
    reportsUrl: process.env.REACT_APP_API_URL + "/commentsReports?commentId=16",
    reviewId: 8,
    reviewUrl: process.env.REACT_APP_API_URL + "/reviews/8",
    spamReportsUrl: process.env.REACT_APP_API_URL + "/commentsReports?commentId=16&reportType=3",
    url: process.env.REACT_APP_API_URL + "/comments/16",
    userUrl: process.env.REACT_APP_API_URL + "/users/Matt",
    username: "Matt"
})

export const reviewDto = new ReviewDto({
    abuseReportsUrl: process.env.REACT_APP_API_URL + "/reviewReports?reviewId=8&reportType=1",
    commentsUrl: process.env.REACT_APP_API_URL + "/comments?reviewId=8",
    hateReportsUrl: process.env.REACT_APP_API_URL + "/reviewReports?reviewId=8&reportType=0",
    id: 8,
    lastModified: "2025-07-05",
    likes: 0,
    likesUrl: process.env.REACT_APP_API_URL + "/reviews/8/likes",
    mediaId: 14,
    mediaUrl: process.env.REACT_APP_API_URL + "/medias/14",
    privacyReportsUrl: process.env.REACT_APP_API_URL + "/reviewReports?reviewId=8&reportType=2",
    rating: 4,
    reportsUrl: process.env.REACT_APP_API_URL + "/reviewReports?reviewId=8",
    reviewContent: "vasfdsdafdsfa",
    spamReportsUrl: process.env.REACT_APP_API_URL + "/reviewReports?reviewId=8&reportType=3",
    url: process.env.REACT_APP_API_URL + "/reviews/8",
    userUrl: process.env.REACT_APP_API_URL + "/users/cane",
    username: "cane"
})

export const userDto = new UserDto({
    banMessageUrl: process.env.REACT_APP_API_URL + "/users/testUser/banMessage",
    defaultPrivateMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser&type=4",
    followedMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?followedByUser=testUser",
    hasBadge: false,
    imageUrl: process.env.REACT_APP_API_URL + "/images/24",
    likedMoovieListsReviewsUrl: process.env.REACT_APP_API_URL + "/moovieListReviews?likedByUser=testUser",
    likedMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?likedByUser=testUser",
    likedReviewsUrl: process.env.REACT_APP_API_URL + "/reviews?likedByUser=testUser",
    milkyPoints: 2,
    moovieListCount: 39,
    moovieListReviewsUrl: process.env.REACT_APP_API_URL + "/moovieListReviews?username=testUser",
    moovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser",
    privateMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser&type=2",
    publicMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser&type=1",
    reviewsCount: 4,
    reviewsUrl: process.env.REACT_APP_API_URL + "/reviews?username=testUser",
    role: 2,
    url: process.env.REACT_APP_API_URL + "/users/testUser",
    username: "testUser"
    }
)

export const userDto2 = new UserDto({
        banMessageUrl: process.env.REACT_APP_API_URL + "/users/testUser2/banMessage",
        defaultPrivateMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser2&type=4",
        followedMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?followedByUser=testUser2",
        hasBadge: false,
        imageUrl: process.env.REACT_APP_API_URL + "/images/25",
        likedMoovieListsReviewsUrl: process.env.REACT_APP_API_URL + "/moovieListReviews?likedByUser=testUser2",
        likedMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?likedByUser=testUser2",
        likedReviewsUrl: process.env.REACT_APP_API_URL + "/reviews?likedByUser=testUser2",
        milkyPoints: 2,
        moovieListCount: 39,
        moovieListReviewsUrl: process.env.REACT_APP_API_URL + "/moovieListReviews?username=testUser2",
        moovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser2",
        privateMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser2&type=2",
        publicMoovieListsUrl: process.env.REACT_APP_API_URL + "/lists?ownerUsername=testUser2&type=1",
        reviewsCount: 4,
        reviewsUrl: process.env.REACT_APP_API_URL + "/reviews?username=testUser2",
        role: 2,
        url: process.env.REACT_APP_API_URL + "/users/testUser2",
        username: "testUser"
    }
)