const MediaOrderBy = {
  NAME: 'name',
  TOTAL_RATING: 'totalRating',
  TMDB_RATING: 'tmdbRating',
  RELEASE_DATE: 'releaseDate',
  VOTE_COUNT: 'voteCount',
  CUSTOM_ORDER: 'customOrder',
};

const MediaOrderByLabels = {
  [MediaOrderBy.NAME]: 'orderBy.name',
  [MediaOrderBy.TOTAL_RATING]: 'orderBy.totalRating',
  [MediaOrderBy.TMDB_RATING]: 'orderBy.tmdbRating',
  [MediaOrderBy.RELEASE_DATE]: 'orderBy.releaseDate',
  [MediaOrderBy.VOTE_COUNT]: 'orderBy.voteCount',
  [MediaOrderBy.CUSTOM_ORDER]: 'orderBy.customOrder',
};

export { MediaOrderBy, MediaOrderByLabels };
